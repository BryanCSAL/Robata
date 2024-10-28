public class RobataOrganizer {

    public static void main(String[] args) {
        try {
            // Exemplo de credenciais e configuração do ChatGPT e do Google Calendar
            String chatGptApiKey = "SUA_API_KEY";
            ChatGPTService chatService = new ChatGPTService(chatGptApiKey);

            Credential credential = GoogleAuthorizeUtil.authorize();
            GoogleCalendarService calendarService = new GoogleCalendarService(credential);

            // Simulação de requisição do usuário
            String userRequest = "Quero agendar uma reunião amanhã às 15h";
            String chatResponse = chatService.generateResponse(userRequest);

            // Processar resposta e criar evento
            if (chatResponse.contains("confirmado")) {
                String eventId = calendarService.scheduleEvent("Reunião", "Reunião de trabalho", "2023-09-12T15:00:00-03:00", "2023-09-12T16:00:00-03:00");
                System.out.println("Evento agendado com ID: " + eventId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
