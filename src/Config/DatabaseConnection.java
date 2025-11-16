/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author eugeniavogt
 */
public class DatabaseConnection {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: No se encontró el driver JDBC de MySQL: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.out.println("No se pudo leer el config.properties, se utilizan valores por defecto: " + e.getMessage());
        }
        String URL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/contribuyentes");
        String USER = props.getProperty("db.user", "root");
        String PASSWORD = props.getProperty("db.password", "");
        if (URL == null || URL.isEmpty() || USER == null || USER.isEmpty() || PASSWORD == null) {
            throw new SQLException("Configuración de la base de datos incompleta o inválida.");
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            throw new SQLException("No se pudo conectar a MySQL: " + e.getMessage());
        }

    }

}
