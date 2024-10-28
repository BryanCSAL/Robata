import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ChatGPTService {
    
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private String apiKey;

    public ChatGPTService(String apiKey) {
        this.apiKey = apiKey;
    }

    public String generateResponse(String userMessage) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String input = "{\"model\":\"gpt-4\",\"prompt\":\"" + userMessage + "\",\"temperature\":0.7,\"max_tokens\":150}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] inputBytes = input.getBytes("utf-8");
            os.write(inputBytes, 0, inputBytes.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
