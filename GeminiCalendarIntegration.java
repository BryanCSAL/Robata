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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiCalendarIntegration {
    private static final String APPLICATION_NAME = "Gemini Google Calendar Integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY"); // Chave carregada de variável de ambiente
    private static final String CREDENTIALS_FILE_PATH = "credentials.json"; // Atualize com o caminho correto
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = List.of("https://www.googleapis.com/auth/calendar.events");

    public static void main(String[] args) {
        try {
            // Prompt enviado ao Gemini
            String prompt = "Agende uma reunião para quinta-feira às 14h com o cliente João.";
            String response = GeminiAPI.getCompletion(prompt);

            // Extração dos detalhes do evento
            EventDetails eventDetails = parseGeminiResponse(response);

            // Inicialização do Google Calendar
            final var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = getCalendarService(HTTP_TRANSPORT);

            // Criação do evento no Google Calendar
            createEvent(service, eventDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cria o evento, adicionando um agendamento a uma data
    /**
     * 
     * O método `convertDayToISODate` é usado para determinar a próxima data correspondente 
     * a um dia da semana especificado, facilitando tarefas como agendamentos, geração de datas futuras, ou qualquer 
     * aplicação onde seja necessário saber a próxima ocorrência de um dia específico no calendário.
     */
    private static void createEvent(Calendar service, EventDetails details) throws IOException {
        Event event = new Event()
                .setSummary(details.title)
                .setStart(new EventDateTime().setDateTime(new DateTime(details.startDateTime)))
                .setEnd(new EventDateTime().setDateTime(new DateTime(details.endDateTime)));

        event = service.events().insert("primary", event).execute();
        System.out.println("Evento criado com sucesso: " + event.getHtmlLink());
    }

    // Utiliza epressões regulares para identificar padrões esperados na resposta
    private static EventDetails parseGeminiResponse(String response) {
        Pattern datePattern = Pattern.compile("(segunda-feira|terça-feira|quarta-feira|quinta-feira|sexta-feira|sábado|domingo)");
        Pattern timePattern = Pattern.compile("(\\d{1,2})h");
        Pattern titlePattern = Pattern.compile("com o (cliente|grupo|time) ([\\w\\s]+)");

        Matcher dateMatcher = datePattern.matcher(response);
        Matcher timeMatcher = timePattern.matcher(response);
        Matcher titleMatcher = titlePattern.matcher(response);

        String date = LocalDate.now().toString(); // Hoje como padrão
        String time = "14:00:00"; // Padrão 14h
        String title = "Reunião";

        if (dateMatcher.find()) {
            date = convertDayToISODate(dateMatcher.group(1));
        }

        if (timeMatcher.find()) {
            time = String.format("%02d:00:00", Integer.parseInt(timeMatcher.group(1)));
        }

        if (titleMatcher.find()) {
            title = "Reunião com " + titleMatcher.group(2).trim();
        }

        String startDateTime = date + "T" + time + "-03:00"; // Adicionando fuso horário UTC-3
        String endDateTime = date + "T" + (Integer.parseInt(time.split(":")[0]) + 1) + ":00:00-03:00"; // Duração de 1h

        return new EventDetails(title, startDateTime, endDateTime);
    }

    //O método `convertDayToISODate` calcula a próxima data no formato ISO 8601 para um dia da semana especificado, partindo da data atual.
    private static String convertDayToISODate(String dayOfWeek) {
        LocalDate today = LocalDate.now();
        DayOfWeek targetDay = DayOfWeek.valueOf(dayOfWeek.toUpperCase().replace("-", "_"));
        int daysUntil = targetDay.getValue() - today.getDayOfWeek().getValue();
        if (daysUntil < 0) {
            daysUntil += 7;
        }
        return today.plusDays(daysUntil).format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static Calendar getCalendarService(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        Credential credentials = getCredentials(HTTP_TRANSPORT);
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
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
    public static String getCompletion(String prompt) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject data = new JSONObject();
        data.put("prompt", prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + System.getenv("GEMINI_API_KEY")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API do Gemini: " + response.statusCode() + " - " + response.body());
        }

        return new JSONObject(response.body()).getString("text");
    }
}
