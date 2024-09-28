import com.google.api.services.calendar.Calendar;

public class Main {
    public static void main(String[] args) {
        try {
            // Envia uma mensagem ao ChatGPT utilizando a classe ChatGPTIntegration.
            // O objetivo é pedir ajuda para marcar uma reunião.
            String chatResponse = ChatGPTIntegration.sendMessage("Olá, preciso marcar uma reunião para segunda-feira.");
            
            // Exibe a resposta recebida do ChatGPT no console.
            System.out.println("Resposta do ChatGPT: " + chatResponse);

            // Integra com o Google Calendar para obter o serviço do calendário autenticado.
            // A função getCalendarService da classe GoogleCalendarIntegration lida com a autenticação.
            Calendar calendarService = GoogleCalendarIntegration.getCalendarService();

            // Cria um evento no Google Calendar usando a função createEvent da classe CalendarEvent.
            // Aqui, um evento com horário e detalhes pré-definidos é inserido no calendário.
            CalendarEvent.createEvent(calendarService);

            // Exibe uma mensagem de confirmação de que o evento foi criado com sucesso no Google Calendar.
            System.out.println("Evento criado com sucesso no Google Calendar!");
        } catch (Exception e) {
            // Captura e imprime no console qualquer exceção que ocorrer durante o processo.
            e.printStackTrace();
        }
    }
}
