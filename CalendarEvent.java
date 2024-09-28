import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

import java.util.TimeZone;

public class CalendarEvent {

    // Método estático para criar e adicionar um evento ao Google Calendar.
    public static void createEvent(Calendar service) throws Exception {
        
        // Cria um novo evento e define seu resumo, local e descrição.
        Event event = new Event()
            .setSummary("Reunião de Teste") // Define o título do evento
            .setLocation("Online")          // Define o local como "Online"
            .setDescription("Reunião de teste via ChatGPT."); // Descrição do evento
        
        // Define a data e hora de início do evento.
        EventDateTime start = new EventDateTime()
            .setDateTime(new DateTime("2024-09-30T10:00:00-07:00")) // Define a data e hora de início
            .setTimeZone(TimeZone.getDefault().getID()); // Define o fuso horário como o padrão do sistema
        event.setStart(start); // Associa a data/hora de início ao evento
        
        // Define a data e hora de término do evento.
        EventDateTime end = new EventDateTime()
            .setDateTime(new DateTime("2024-09-30T11:00:00-07:00")) // Define a data e hora de término
            .setTimeZone(TimeZone.getDefault().getID()); // Usa o mesmo fuso horário do início
        event.setEnd(end); // Associa a data/hora de término ao evento
        
        // Insere o evento no calendário primário do usuário e executa a operação.
        service.events().insert("primary", event).execute();
    }
}
