import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.InputStreamReader;
import java.util.Collections;
import java.io.InputStream;

public class GoogleCalendarIntegration {
    // Nome da aplicação, que será usado ao interagir com a API do Google Calendar
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    
    // Formato de JSON utilizado pelo JacksonFactory para lidar com os dados da API
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    // Pasta onde as credenciais serão armazenadas localmente no sistema
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");

    /**
     * Este método é responsável por obter as credenciais necessárias para acessar a API do Google Calendar.
     * Ele realiza a autenticação OAuth2, o que envolve carregar as credenciais do cliente e solicitar
     * autorização do usuário para acessar sua conta do Google Calendar.
     */
    public static Credential getCredentials() throws Exception {
        // Carrega o arquivo de segredo do cliente ("client secrets") que contém as credenciais do Google
        InputStream in = GoogleCalendarIntegration.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Cria o fluxo de autorização OAuth2, configurando o transporte seguro e o armazenamento local
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, 
                Collections.singleton(CalendarScopes.CALENDAR)) // Solicita acesso ao escopo do Google Calendar
                .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER)) // Armazena tokens offline
                .setAccessType("offline") // Garante que o token de atualização seja obtido para acesso offline
                .build();

        // Abre uma janela de navegador para o usuário autorizar a aplicação a acessar sua conta
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Este método configura o serviço do Google Calendar usando as credenciais obtidas pelo método anterior.
     * Ele retorna uma instância do serviço Calendar, que permite interagir com os eventos do calendário do usuário.
     */
    public static Calendar getCalendarService() throws Exception {
        // Obtenção das credenciais do usuário
        Credential credential = getCredentials();
        
        // Cria e retorna uma instância do serviço Google Calendar, autenticada com as credenciais fornecidas
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME) // Define o nome da aplicação na API
                .build();
    }
}
