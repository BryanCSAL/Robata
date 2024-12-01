package servlet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.calendar.Calendar;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import process.CalendarService;
import process.EventDetails;
import process.GeminiService;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.logging.Logger;

@WebServlet(name = "AgendamentoServlet", urlPatterns = {"/AgendamentoServlet"})
public class AgendamentoServlet extends HttpServlet {
    private final CalendarService calendarService = new CalendarService();
    private final GeminiService geminiService = new GeminiService();
    private static final LocalDate dataAtual = LocalDate.now();
    private static final DayOfWeek diaDaSemana = dataAtual.getDayOfWeek();
    private static final Logger LOGGER = Logger.getLogger(AgendamentoServlet.class.getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8"); // Garantir codificação UTF-8
        
        CalendarService.validateEnvironmentVariables();
        
        try {
            String userPrompt = request.getParameter("userPrompt");
            LOGGER.info("Parâmetro recebido: " + userPrompt);
            if (userPrompt == null || userPrompt.trim().isEmpty()) {
                LOGGER.warning("Parâmetro 'userPrompt' está vazio.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("O parâmetro 'userPrompt' é obrigatório.");
                return;
            }


            String prompt = "Reescreva esta mensagem:" + userPrompt +
                    " para que siga este padrão: O título deve começar com 'Assunto:' seguido por 'Reunião' e o tema do evento. " +
                    "Inclua uma data no formato 'dd de [mês por extenso]' (por exemplo, '25 de novembro'). " +
                    "Inclua um horário no formato 'hhh' (por exemplo, '14h'). A estrutura deve ser clara e os campos apresentados como linhas separadas." +
                    "Lembrando que hoje é " + dataAtual + " " + diaDaSemana;

            String geminiResponse = geminiService.getCompletion(prompt);
            LOGGER.info("Resposta do Gemini: " + geminiResponse);

            if (geminiResponse == null || geminiResponse.trim().isEmpty()) {
                throw new Exception("A resposta do Gemini está vazia ou inválida.");
            }

            EventDetails eventDetails = calendarService.parseGeminiResponse(geminiResponse);

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = CalendarService.getCalendarService(HTTP_TRANSPORT);

            calendarService.createEvent(service, eventDetails);
            LOGGER.info("Evento criado com sucesso.");

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Evento criado com sucesso!\n" + geminiResponse);
        } catch (Exception e) {
            LOGGER.severe("Erro ao processar requisição: " + e.getMessage());
            e.printStackTrace(); // Adicione para depuração
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao processar sua solicitação. Detalhes: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        processRequest(request, response);
    }
}
