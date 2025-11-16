package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author eugeniavogt
 */
public class TestConection {

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conectado a URL: " + conn.getMetaData().getURL());
            System.out.println("Base de datos: " + conn.getCatalog());
            System.out.println("Schema: " + conn.getSchema());
            if (conn != null) {
                System.out.println("Conexión a mysql exitosa");
                String sql = "SELECT * FROM domicilioFiscal";
                try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        System.out.println("Id " + id);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al conectarse a mysql" + e.getMessage());
            e.printStackTrace();
        }
    }

}
