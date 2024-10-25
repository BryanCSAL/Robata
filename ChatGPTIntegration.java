import okhttp3.*; // Importa a biblioteca OkHttp para fazer requisições HTTP
import java.io.IOException; // Importa exceções de entrada/saída

public class ChatGPTIntegration {

    // Constante contendo a chave de API para autenticação no ChatGPT
    private static final String API_KEY = "CHAVE_DE_API";    // COLOCAR CHAVE DA API DO GPT
    
    // URL da API do ChatGPT onde as requisições serão enviadas
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    // Método que envia uma mensagem (prompt) para a API do ChatGPT e retorna a resposta
    public static String sendMessage(String prompt) throws IOException {
        
        // Cria uma nova instância de OkHttpClient, que é responsável por fazer a requisição HTTP
        OkHttpClient client = new OkHttpClient();

        // Define o tipo de mídia como JSON, necessário para a requisição HTTP
        MediaType mediaType = MediaType.parse("application/json");
        
        // Cria o corpo da requisição, passando o modelo GPT-4 e o prompt do usuário
        RequestBody body = RequestBody.create(mediaType, 
            "{ \"model\": \"gpt-4\", \"messages\": [{ \"role\": \"user\", \"content\": \"" + prompt + "\" }]}");

        // Constrói a requisição HTTP com a URL da API, método POST, corpo da requisição e cabeçalhos
        Request request = new Request.Builder()
            .url(API_URL) // URL da API
            .post(body)   // Define o método POST para enviar dados à API
            .addHeader("Authorization", "Bearer " + API_KEY) // Cabeçalho de autorização com a chave da API
            .addHeader("Content-Type", "application/json")   // Define o tipo de conteúdo como JSON
            .build(); // Constrói o objeto Request

        // Executa a requisição HTTP e recebe a resposta
        Response response = client.newCall(request).execute();
        
        // Retorna o corpo da resposta como uma string
        return response.body().string();
    }
}
