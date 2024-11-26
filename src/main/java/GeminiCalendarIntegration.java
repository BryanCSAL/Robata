import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import org.json.JSONObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import java.time.LocalDate;
import java.time.DayOfWeek;

public class GeminiCalendarIntegration {
    private static final String APPLICATION_NAME = "Gemini Google Calendar Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\BRYAN\\Documents\\NetBeansProjects\\ROBATA\\src\\main\\resources\\credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/calendar.events");
    private static final Logger logger = Logger.getLogger(GeminiCalendarIntegration.class.getName());
    private static final LocalDate dataAtual = LocalDate.now();
    private static final DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();

    public static void main(String[] args) {
        try {
            // Validar a chave da API
            validateEnvironmentVariables();

            // Enviar prompt ao Gemini
            String user_prompt = "Marca uma reunião pra proxima quinta 2 da tarde com o cliente Mamario.";
            String prompt = "Reescreva esta mensagem:" + user_prompt +
                    " para que siga este padrão: O título deve começar com 'Assunto:' seguido por 'Reunião' e o tema do evento. " +
                    "Inclua uma data no formato 'dd de [mês por extenso]' (por exemplo, '25 de novembro'). " +
                    "Inclua um horário no formato 'hhh' (por exemplo, '14h'). A estrutura deve ser clara e os campos apresentados como linhas separadas." +
                    "Lembrando que hoje é " + dataAtual + " " + diaDaSemana ;
            String response = GeminiAPI.getCompletion(prompt);

            // Registrar resposta completa para depuração
            System.out.println("Resposta do Gemini: " + response);

            // Extrair os detalhes do evento da resposta do Gemini
            EventDetails eventDetails = parseGeminiResponse(response);

            // Inicializar o Google Calendar
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = getCalendarService(HTTP_TRANSPORT);

            // Criar evento no Google Calendar
            createEvent(service, eventDetails);

            logger.info("Execução concluída com sucesso.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro durante a execução: ", e);
        }
    }

    // 
    private static void createEvent(Calendar service, EventDetails details) throws IOException {
        Event event = new Event()
                .setSummary(details.title)
                .setStart(new EventDateTime().setDateTime(new DateTime(details.startDateTime)))
                .setEnd(new EventDateTime().setDateTime(new DateTime(details.endDateTime)));

        event = service.events().insert("primary", event).execute();
        System.out.println("Evento criado com sucesso: " + event.getHtmlLink());
    }

    private static EventDetails parseGeminiResponse(String response) {
    // Padrões para extrair informações
    Pattern titlePattern = Pattern.compile("Assunto:\\s*(Reunião.*)");
    Pattern datePattern = Pattern.compile("(\\d{1,2}) de \\[?([a-zA-Zçã]+|Mês)\\]?");
    Pattern timePattern = Pattern.compile("(\\d{1,2})h");

    // Matchers para encontrar os dados na resposta
    Matcher titleMatcher = titlePattern.matcher(response);
    Matcher dateMatcher = datePattern.matcher(response);
    Matcher timeMatcher = timePattern.matcher(response);

    String title = null;
    String date = null;
    String time = null;

    // Extraindo o título
    if (titleMatcher.find()) {
        title = titleMatcher.group(1).trim();
    } else {
        System.err.println("Aviso: Não foi possível identificar o título.");
    }

    // Extraindo a data
    if (dateMatcher.find()) {
        String day = dateMatcher.group(1);
        String month = dateMatcher.group(2).toLowerCase();
        System.out.println("Mês extraído: " + month);

        // Validando e convertendo o mês
        if (month.equals("mês") || month.length() < 3) {
            System.err.println("Erro: Mês inválido ou incompleto. Usando padrão genérico.");
            month = "janeiro"; // Definindo janeiro como fallback
        }

        try {
            date = convertFullDateToISO(day, month);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao converter a data: " + e.getMessage());
        }
    } else {
        System.err.println("Aviso: Não foi possível identificar a data.");
    }

    // Extraindo o horário
    if (timeMatcher.find()) {
        time = String.format("%02d:00:00", Integer.parseInt(timeMatcher.group(1)));
    } else {
        System.err.println("Aviso: Não foi possível identificar o horário.");
    }

    // Validando os dados extraídos
    if (date == null || time == null || title == null) {
        throw new IllegalArgumentException("Detalhes insuficientes para criar o evento: " + response);
    }

    // Montando o horário completo com fuso
    String zoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now()).toString();
    String startDateTime = date + "T" + time + zoneOffset;
    String endDateTime = date + "T" + (Integer.parseInt(time.split(":")[0]) + 1) + ":00:00" + zoneOffset;

    return new EventDetails(title, startDateTime, endDateTime);
}

private static String convertFullDateToISO(String day, String month) {
    // Mapeamento de meses
    Map<String, String> monthMap = Map.ofEntries(
            Map.entry("janeiro", "01"),
            Map.entry("fevereiro", "02"),
            Map.entry("março", "03"),
            Map.entry("abril", "04"),
            Map.entry("maio", "05"),
            Map.entry("junho", "06"),
            Map.entry("julho", "07"),
            Map.entry("agosto", "08"),
            Map.entry("setembro", "09"),
            Map.entry("outubro", "10"),
            Map.entry("novembro", "11"),
            Map.entry("dezembro", "12")
    );

    // Verificando se o mês é válido
    String monthNumber = monthMap.getOrDefault(month, null);
    if (monthNumber == null) {
        throw new IllegalArgumentException("Mês inválido: " + month);
    }

    int year = Year.now().getValue(); // Ano atual
    return String.format("%d-%s-%02d", year, monthNumber, Integer.parseInt(day));
}


    private static Calendar getCalendarService(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        Credential credentials = getCredentials(HTTP_TRANSPORT);
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        if (!credentialsFile.exists()) {
            throw new FileNotFoundException("Arquivo de credenciais não encontrado: " + CREDENTIALS_FILE_PATH);
        }
        InputStream in = new FileInputStream(credentialsFile);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static void validateEnvironmentVariables() {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isBlank()) {
            throw new IllegalStateException("Chave de API do Gemini não encontrada. Configure a variável de ambiente GEMINI_API_KEY.");
        }
    }
}

class EventDetails {
    String title;
    String startDateTime;
    String endDateTime;

    public EventDetails(String title, String startDateTime, String endDateTime) {
        this.title = title;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}

class GeminiAPI {
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");

    public static String getCompletion(String prompt) throws IOException, InterruptedException {
        var jsonRequest = "{"
                + "\"contents\": ["
                + "    {"
                + "        \"parts\": ["
                + "            {"
                + "                \"text\": \"" + prompt + "\""
                + "            }"
                + "        ],"
                + "        \"role\": \"user\""
                + "    }"
                + "]"
                + "}";

        var httpClient = HttpClient.newHttpClient();
        var apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + GEMINI_API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API do Gemini: " + response.statusCode() + " - " + response.body());
        }

        return processResponse(response.body());
    }

    private static String processResponse(String responseBody) {
    JSONObject jsonObject = new JSONObject(responseBody);

    // Verifique se existem candidatos na resposta
    if (!jsonObject.has("candidates") || jsonObject.getJSONArray("candidates").isEmpty()) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Nenhum candidato encontrado.");
    }

    // Pegue o primeiro candidato
    JSONObject candidate = jsonObject.getJSONArray("candidates").getJSONObject(0);

    // Navegue até "content.parts" e pegue o texto
    if (!candidate.has("content") || !candidate.getJSONObject("content").has("parts")) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Estrutura 'content.parts' não encontrada.");
    }

    JSONArray parts = candidate.getJSONObject("content").getJSONArray("parts");

    if (parts.isEmpty()) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Nenhum texto encontrado em 'parts'.");
    }

    // Retorne o texto da primeira parte
    return parts.getJSONObject(0).getString("text");
}

}


/**
 * Fazer a entrada de dados passar pelo gemini, afim de faze-lo processar e formartar os dados,
 * de maneira que fique de facil entendimento para o programa gerar o json.
 * 
 * Adicionar entrada de prompt:
 * 
 * Entrada_usuario: "Agende uma reunião para dia 30 às 14h com o cliente João."
 * 
 * Prompt: "Reescreva esta mensagem:" + Entrada_usuario + "para que siga este padrão:
    O título deve começar com "Assunto:" seguido por "Reunião" e o tema do evento.
    Inclua uma data no formato "dd de [mês]" (por exemplo, "25 de novembro").
    Inclua um horário no formato "hhh" (por exemplo, "14h"). A estrutura deve ser clara e os campos apresentados como linhas separadas."
 */