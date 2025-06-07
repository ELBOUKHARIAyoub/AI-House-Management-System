package org.example.householdledger.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.sql.*;

import org.example.householdledger.Model.DBConnection;
import org.json.JSONObject;
import org.json.JSONArray;

public class DeepSeekService {
    private static final String API_KEY = "ZBI LA DITIH";
    private static final String ENDPOINT = "https://dseek.aikeji.vip/v1/chat/completions";


    public static String askDeepSeek(String userQuestion) throws IOException, InterruptedException {
        String schema = """
                Table `user`:
                  - userId (int)
                  - username (text)
                  - password (text)
                  - balance (real)

                Table `expense`:
                  - expenseId (int)
                  - userId (int)
                  - expenseName (text)
                  - expenseAmount (real)
                  - expenseDemo (text)

                Table `income`:
                  - incomeId (int)
                  - userId (int)
                  - name (text)
                  - amount (real)
                  - demo (text)
                  - date (date)

                Table `budget`:
                  - budgetId (int)
                  - userId (int)
                  - category (text)
                  - budgetAmount (real)
                  - spentAmount (real)
                  - progress (real)
                """;
        String prompt;
if(userQuestion.startsWith("ask")){
        prompt =userQuestion;
}else{
            prompt = "You are a MYSQL assistant. Based on this schema:\n" +
                    schema + "\n" +
                    "Generate a MySQL query for the user question: \"" + userQuestion + "\"\n" +
                    "Do not use square brackets ([]) .\n" +
                    "Don't use aliases or AS clauses. Just return the SQL query only.";
        }
        // Debug print
        System.out.println("DeepSeek Prompt (askDeepSeek):\n" + prompt);

        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", "You are a SQL assistant"))
                .put(new JSONObject().put("role", "user").put("content", prompt));

        JSONObject requestBody = new JSONObject()
                .put("model", "deepseek-chat")
                .put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject res = new JSONObject(response.body());

        // Debug print
        System.out.println("DeepSeek Response (askDeepSeek):\n" + res.toString(2));

        if (!res.has("choices")) {
            return "Error: 'choices' field not found in response.\nFull response:\n" + res.toString(2);
        }

        return res.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }

    public static String executeSQL(String sql, Object... params) {
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBConnection.getConnection()) {

            if (sql.trim().toLowerCase().startsWith("select")) {
                PreparedStatement stmt = conn.prepareStatement(sql);

                // Set parameters
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }

                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                int columns = meta.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= columns; i++) {
                        result.append(meta.getColumnLabel(i)).append(": ").append(rs.getString(i)).append("\n");
                    }
                    result.append("\n");
                }

                if (result.toString().isBlank()) {
                    return "No results found.";
                }

            } else {
                PreparedStatement stmt = conn.prepareStatement(sql);

                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }

                int affectedRows = stmt.executeUpdate();
                result.append("Statement executed successfully. Rows affected: ").append(affectedRows);
            }

        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        }

        return result.toString();
    }


    public static String generateAnswer(String question, String result) throws IOException, InterruptedException {
        JSONArray messages = new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", "You are a financial assistant"))
                .put(new JSONObject().put("role", "user").put("content", "Question: " + question + "\nData:\n" + result));

        JSONObject body = new JSONObject()
                .put("model", "deepseek-chat")
                .put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ENDPOINT))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject res = new JSONObject(response.body());

        // Debug print
        System.out.println("DeepSeek Response (generateAnswer):\n" + res.toString(2));

        if (!res.has("choices")) {
            return "Error: 'choices' field not found in response.\nFull response:\n" + res.toString(2);
        }

        return res.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }
}
