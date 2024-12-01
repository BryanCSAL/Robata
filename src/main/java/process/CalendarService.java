/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package process;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.*;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author BRYAN
 */
public class CalendarService {
    private static final String APPLICATION_NAME = "Gemini Google Calendar Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\BRYAN\\Documents\\NetBeansProjects\\ProjetoFinalPUPO\\src\\main\\resources\\credentials.json";
    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/calendar.events");
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");
    
    public static void createEvent(Calendar service, EventDetails details) throws IOException {
        Event event = new Event()
                .setSummary(details.title)
                .setStart(new EventDateTime().setDateTime(new DateTime(details.startDateTime)))
                .setEnd(new EventDateTime().setDateTime(new DateTime(details.endDateTime)));

        event = service.events().insert("primary", event).execute();
        System.out.println("Evento criado com sucesso: " + event.getHtmlLink());
    }

    public static EventDetails parseGeminiResponse(String response) {
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


    public static Calendar getCalendarService(NetHttpTransport HTTP_TRANSPORT) throws IOException {
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
    
    public static void validateEnvironmentVariables() {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isBlank()) {
            throw new IllegalStateException("Chave de API do Gemini não encontrada. Configure a variável de ambiente GEMINI_API_KEY.");
        }
    }

}
