/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package process;


import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author BRYAN
 */
public class GeminiService {
    private static final String GEMINI_API_KEY = System.getenv("GEMINI_API_KEY");

    public static String getCompletion(String prompt) throws IOException, InterruptedException {
        var jsonRequest = "{"
                + "\"contents\": ["
                + "    {"
                + "        \"parts\": ["
                + "            {"
                + "                \"text\": \"" + prompt + "\""
                + "            }"
                + "        ],"
                + "        \"role\": \"user\""
                + "    }"
                + "]"
                + "}";

        var httpClient = HttpClient.newHttpClient();
        var apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

        var request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + GEMINI_API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API do Gemini: " + response.statusCode() + " - " + response.body());
        }

        return processResponse(response.body());
    }

    private static String processResponse(String responseBody) {
    JSONObject jsonObject = new JSONObject(responseBody);

    // Verifique se existem candidatos na resposta
    if (!jsonObject.has("candidates") || jsonObject.getJSONArray("candidates").isEmpty()) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Nenhum candidato encontrado.");
    }

    // Pegue o primeiro candidato
    JSONObject candidate = jsonObject.getJSONArray("candidates").getJSONObject(0);

    // Navegue até "content.parts" e pegue o texto
    if (!candidate.has("content") || !candidate.getJSONObject("content").has("parts")) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Estrutura 'content.parts' não encontrada.");
    }

    JSONArray parts = candidate.getJSONObject("content").getJSONArray("parts");

    if (parts.isEmpty()) {
        throw new RuntimeException("Resposta inesperada da API do Gemini: Nenhum texto encontrado em 'parts'.");
    }

    // Retorne o texto da primeira parte
    return parts.getJSONObject(0).getString("text");
}

    public EventDetails processPrompt(String userPrompt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

