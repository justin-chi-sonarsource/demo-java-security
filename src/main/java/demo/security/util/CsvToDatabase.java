package demo.security.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CsvToDatabase {
    private String jdbcUrl;
    private String username;
    private String password;

    public CsvToDatabase(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void importCsv(String csvFilePath) {
        String line;
        String cvsSplitBy = ",";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String sql = "INSERT INTO your_table (column1, column2, column3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                pstmt.setString(1, data[0]);
                pstmt.setString(2, data[1]);
                pstmt.setString(3, data[2]);
                pstmt.executeUpdate();
            }
            pstmt.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_db";
        String username = "your_user";
        String password = "your_password";
        String csvFilePath = "path/to/your.csv";
        CsvToDatabase importer = new CsvToDatabase(jdbcUrl, username, password);
        importer.importCsv(csvFilePath);
    }
}
