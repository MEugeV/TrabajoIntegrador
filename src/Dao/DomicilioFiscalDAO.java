/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DatabaseConnection;
import Models.DomicilioFiscal;
import Models.Pais;
import Models.Provincia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eugeniavogt 
 * Persistencia de operaciones CRUD de Entidad DomicilioFiscal en la Base de Datos
 * Utiliza preparedStatements para prevenir inyección SQL
 * Implementa eliminado lógico Try with resources para cierre automático de recursos
 * Las operaciones de inserción, creación, eliminación y recupero se realizan desde la Entidad Principal Empresa para mantener integridad referenial
 */
public class DomicilioFiscalDAO implements GenericDAO<DomicilioFiscal> {

    private static final String INSERT_SQL = "INSERT INTO domicilioFiscal (calle, numero, ciudad, provincia, codigoPostal, pais) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE domicilioFiscal SET calle = ?, numero = ?, ciudad = ?, provincia = ?, codigoPostal = ?, pais = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE domicilioFiscal SET eliminado = TRUE WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM domicilioFiscal WHERE id = ? AND eliminado = FALSE";
    private static final String SELECT_ALL_SQL = "SELECT * FROM domicilioFiscal WHERE eliminado = FALSE";
    private static final String RESET_SQL = "UPDATE domicilioFiscal SET eliminado = FALSE WHERE id = ?";

    private void setStatementParameters(PreparedStatement stmt, DomicilioFiscal domicilio) throws SQLException {
        stmt.setString(1, domicilio.getCalle());
        stmt.setInt(2, domicilio.getNumero());
        stmt.setString(3, domicilio.getCiudad());
        stmt.setString(4, domicilio.getProvincia().getLabel());
        stmt.setString(5, domicilio.getCodigoPostal());
        stmt.setString(6, domicilio.getPais().getLabel());
    }

    public void setDomicilioFiscal(ResultSet rs, DomicilioFiscal domicilio) throws SQLException {
        domicilio.setId(rs.getInt("id"));
        domicilio.setCalle(rs.getString("calle"));
        domicilio.setNumero(rs.getInt("numero"));
        domicilio.setCiudad(rs.getString("ciudad"));
        domicilio.setProvincia(Provincia.fromLabel(rs.getString("provincia")));
        domicilio.setCodigoPostal(rs.getString("codigoPostal"));
        domicilio.setPais(Pais.fromLabel(rs.getString("pais")));
        domicilio.setEliminado(rs.getBoolean("eliminado"));
    }

    @Override
    public void insertar(DomicilioFiscal entidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);) {
            setStatementParameters(stmt, entidad);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getInt(1));
                    System.out.println("Domicilio fiscal insertado con ID: " + entidad.getId());
                } else {
                    throw new SQLException("La inserción del domicilio fiscal falló, no se obtuvo ID generado ");
                }
            }
        }
    }

    @Override
    public void actualizar(DomicilioFiscal entidad) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL);) {
            setStatementParameters(stmt, entidad);
            stmt.setInt(7, entidad.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo deshacer la actualización del domicilio fiscal " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(DELETE_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer eliminación lógica del domicilio fiscal con ID " + id + e.getMessage());
        }
    }

    @Override
    public DomicilioFiscal getById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DomicilioFiscal domicilio = new DomicilioFiscal();
                setDomicilioFiscal(rs, domicilio);
                return domicilio;

            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener domicilio fiscal por ID " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<DomicilioFiscal> getAll() throws SQLException {
        List<DomicilioFiscal> domicilios = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL); ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                DomicilioFiscal domicilio = new DomicilioFiscal();
                setDomicilioFiscal(rs, domicilio);
                domicilios.add(domicilio);

            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener todos los domicilios fiscales " + e.getMessage());
        }
        return domicilios;
    }

    @Override
    public void recuperar(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(RESET_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo deshacer la eliminación lógica del domicilio fiscal con ID " + id + " " + e.getMessage());
        }
    }

    @Override
    public void insertarTx(DomicilioFiscal entidad, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);) {
            setStatementParameters(stmt, entidad);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getInt(1));
                    System.out.println("Domicilio fiscal insertado con ID: " + entidad.getId());
                } else {
                    throw new SQLException("La inserción de la domicilio fiscal en transacción falló, no se obtuvo ID generado ");
                }
            }
        }
    }

    @Override
    public void actualizarTx(DomicilioFiscal entidad, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL);) {
            setStatementParameters(stmt, entidad);
            stmt.setInt(7, entidad.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer actualización en transacción del domicilio fiscal " + e.getMessage());
        }
    }

    @Override
    public void eliminarTx(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer eliminación lógica en transacción del domicilio fiscal con ID " + id + e.getMessage());
        }
    }

    @Override
    public void recuperarTx(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(RESET_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo deshacer la recuperación lógica en transacción del domicilio fiscal con ID " + id + " " + e.getMessage());
        }
    }

}
