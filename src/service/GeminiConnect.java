package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class GeminiConnect {
	private static final String apiKey = "AIzaSyBABuTzfaEyA6F2i7kkRoz4Ez2wb08PV0o";
    private static final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;



    // Convert image file to base64 string
    private static String convertImageToBase64(String imagePath) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // Write the response to a text file
    private static void writeToFile(String content, String fileName) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }

	
 // Hoàn thiện hàm generateAnswerFromImage
    public static String generateAnswerFromImage(String imagePath, String promptText) {
        try {
           
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Convert image to base64
            String base64Image = convertImageToBase64(imagePath);
            // Build request JSON
            String request = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\n" +
                    "          \"text\": \"" + promptText + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"inlineData\": {\n" +
                    "            \"mimeType\": \"image/png\",\n" +
                    "            \"data\": \"" + base64Image + "\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = request.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read response
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return parseResponse(response.toString());
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.err.println("Error Response (Code " + code + "): " + errorResponse.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String generateSuggestAnswer(String prompt) {
        try {
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON request payload
            String request = "{\n" +
                    "  \"contents\": [\n" +
                    "    {\n" +
                    "      \"parts\": [\n" +
                    "        {\n" +
                    "          \"text\": \"" + prompt + "\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = request.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get response
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    return parseResponse(response.toString());
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    throw new Exception("Error Response (Code " + code + "): " + errorResponse.toString());
                }
            }
        } catch (Exception ex) {
            System.out.println("Failed to get AI suggestion: " + ex.getMessage());
            return "Error: Failed to get AI suggestion - " + ex.getMessage();
        }
    }

    private static String parseResponse(String jsonResponse) {
        try {
            // Find the start of the "text" field
            int start = jsonResponse.indexOf("\"text\": \"") + 9;
            if (start < 9) {
                return "Error: Unable to find 'text' field in response.";
            }

            // Find the end of the text content by looking for the closing quote
            // We need to handle escaped quotes within the text
            int end = start;
            boolean inEscape = false;
            while (end < jsonResponse.length()) {
                char c = jsonResponse.charAt(end);
                if (c == '\\') {
                    inEscape = true;
                    end++;
                    continue;
                }
                if (c == '"' && !inEscape) {
                    break;
                }
                inEscape = false;
                end++;
            }

            if (end >= jsonResponse.length() || end <= start) {
                return "Error: Unable to parse AI response - invalid text field.";
            }

            // Extract the text and replace escaped newlines with actual newlines
            String text = jsonResponse.substring(start, end);
            text = text.replace("\\n", "\n").replace("\\\"", "\"");
            return text;
        } catch (Exception e) {
            return "Error: Unable to parse AI response - " + e.getMessage();
        }
    }
   
       
    


   
}