import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "RobataOrganizer";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private Calendar service;

    public GoogleCalendarService(Credential credential) throws GeneralSecurityException, IOException {
        service = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String scheduleEvent(String summary, String description, String startDateTime, String endDateTime) throws IOException {
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTime start = new DateTime(startDateTime);
        event.setStart(new EventDateTime().setDateTime(start).setTimeZone("America/Sao_Paulo"));

        DateTime end = new DateTime(endDateTime);
        event.setEnd(new EventDateTime().setDateTime(end).setTimeZone("America/Sao_Paulo"));

        event = service.events().insert("primary", event).execute();
        return event.getId();
    }
}
