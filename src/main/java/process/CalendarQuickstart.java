package process;



import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/* Classe */
public class CalendarQuickstart {
  /**
   * Nome da aplicação.
   */
  private static final String APPLICATION_NAME = "ROBATA";
  /**   
   * Instancia Global do JSON factory.
   */
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  /**
   * Diretório que guarda os tokens de autorização desta aplicação.
   */
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Instancia global do escopo necessário para rodar a aplicação.
   */
  private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS);
  private static final String CREDENTIALS_FILE_PATH = "credentials.json";

  /**
   * Criando um objeto, a credencial autorizada.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    System.out.println("Iniciando processo de autenticacao..."); // Adicione este log

    // Carrega o client_secret
    InputStream in = CalendarQuickstart.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
        throw new FileNotFoundException("Recurso não encontrado: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    
    // Constrói o fluxo de autorização
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9090).build();
    System.out.println("Iniciando a autorizacao...");
    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    System.out.println("Autenticacao concluida com sucesso.");

    return credential;
  }

  public static void main(String... args) throws IOException, GeneralSecurityException {
    System.out.println("Iniciando o serviço Google Calendar...");

    // Exclui o diretório de tokens, se existente
    File tokensDirectory = new File(TOKENS_DIRECTORY_PATH);
    if (tokensDirectory.exists()) {
        deleteDirectory(tokensDirectory);
        System.out.println("Diretório de tokens excluído com sucesso.");
    } else {
        System.out.println("Diretório de tokens não encontrado.");
    }

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    Calendar service =
        new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    
    System.out.println("Serviço Google Calendar inicializado.");
  }

  // Método recursivo para excluir o diretório e todos os arquivos dentro dele
  private static void deleteDirectory(File directory) {
    File[] files = directory.listFiles();
    if (files != null) {
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file); // Chamada recursiva para subdiretórios
            } else {
                file.delete(); // Exclui arquivos
            }
        }
    }
    directory.delete(); // Exclui o diretório
  }
}
