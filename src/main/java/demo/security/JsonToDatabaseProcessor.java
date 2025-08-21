package demo.security;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToDatabaseProcessor {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    public static void main(String[] args) {
        String jsonFilePath = "s3649JavaSqlInjectionConfig.json"; // Update path if needed
        ObjectMapper mapper = new ObjectMapper();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            JsonNode root = mapper.readTree(new File(jsonFilePath));
            for (JsonNode node : root) {
                // Example: assuming each node has 'id' and 'name' fields
                int id = node.get("id").asInt();
                String name = node.get("name").asText();
                saveToDatabase(conn, id, name);
            }
            System.out.println("Data processed and saved to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToDatabase(Connection conn, int id, String name) throws SQLException {
        String sql = "INSERT INTO your_table (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.executeUpdate();
        }
    }
}
