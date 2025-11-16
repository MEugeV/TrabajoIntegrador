/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Models.Empresa;
import Models.DomicilioFiscal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import Config.DatabaseConnection;
import Models.Provincia;
import Models.Pais;
import Models.ActividadPrincipal;
import java.util.ArrayList;

/**
 *
 * @author eugeniavogt Persistencia de operaciones CRUD de Entidad Empresa en la
 * Base de Datos Utiliza preparedStatements para prevenir inyección SQL
 * Implementa eliminado lógico Try with resources para cierre automático de
 * recursos - excepto en transacciones, en las cuales recibe conexión externa
 * Filtro automático por borrado lógico, con excepciones implementadas mediante overloading de métodos, para operación de recuperación
 */
public class EmpresaDAO implements GenericDAO<Empresa> {

    private static final String INSERT_SQL = "INSERT INTO empresa (razonSocial, cuit, actividadPrincipal, email, domicilioFiscalId) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE empresa SET razonSocial = ?, cuit = ?, actividadPrincipal = ?, email = ?, domicilioFiscalId = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE empresa SET eliminado = TRUE WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT e.id, e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscalId, e.eliminado as e_elim, "
            + "df.id AS df_id, df.calle, df.numero, df.ciudad, df.provincia, df.codigoPostal, df.pais, df.eliminado as df_elim "
            + "FROM empresa e LEFT JOIN domicilioFiscal df ON e.domicilioFiscalId = df.id "
            + "WHERE e.id = ?";
    private static final String SELECT_ALL_SQL = "SELECT e.id, e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscalId, e.eliminado as e_elim, "
            + "df.id AS df_id, df.calle, df.numero, df.ciudad, df.provincia, df.codigoPostal, df.pais, df.eliminado as df_elim "
            + "FROM empresa e LEFT JOIN domicilioFiscal df ON e.domicilioFiscalId = df.id "
            + "WHERE e.eliminado = FALSE";
    private static final String RESET_SQL = "UPDATE empresa SET eliminado = FALSE WHERE id = ?";
    private static final String SEARCH_BY_RAZON_SOCIAL_SQL = "SELECT e.id, e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscalId, e.eliminado as e_elim, "
            + "df.id AS df_id, df.calle, df.numero, df.ciudad, df.provincia, df.codigoPostal, df.pais, df.eliminado as df_elim "
            + "FROM empresa e LEFT JOIN domicilioFiscal df ON e.domicilioFiscalId = df.id "
            + "WHERE e.eliminado = FALSE AND (e.razonSocial LIKE ? )";
    private static final String SEARCH_BY_CUIT_SQL = "SELECT e.id, e.razonSocial, e.cuit, e.actividadPrincipal, e.email, e.domicilioFiscalId, e.eliminado as e_elim, "
            + "df.id AS df_id, df.calle, df.numero, df.ciudad, df.provincia, df.codigoPostal, df.pais, df.eliminado as df_elim "
            + "FROM empresa e LEFT JOIN domicilioFiscal df ON e.domicilioFiscalId = df.id "
            + "WHERE e.cuit = ?";

    private void setStatementParameters(PreparedStatement stmt, Empresa empresa) throws SQLException {
        stmt.setString(1, empresa.getRazonSocial());
        stmt.setString(2, empresa.getCuit());
        stmt.setString(3, empresa.getActividadPrincipal().getLabel());
        stmt.setString(4, empresa.getEmail());
        if (empresa.getDomicilioFiscal() != null && empresa.getDomicilioFiscal().getId() > 0) {
            stmt.setInt(5, empresa.getDomicilioFiscal().getId());
        } else {
            stmt.setNull(5, java.sql.Types.INTEGER);
        }
    }

    public void setEmpresa(ResultSet rs, Empresa empresa) throws SQLException {
        empresa.setId(rs.getInt("id"));
        empresa.setRazonSocial(rs.getString("razonSocial"));
        empresa.setCuit(rs.getString("cuit"));
        empresa.setActividadPrincipal(ActividadPrincipal.fromLabel(rs.getString("actividadPrincipal")));
        empresa.setEmail(rs.getString("email"));
        empresa.setEliminado(rs.getBoolean("e_elim"));
    }

    public void setDomicilio(ResultSet rs, DomicilioFiscal domicilio) throws SQLException {
        domicilio.setId(rs.getInt("df_id"));
        domicilio.setCalle(rs.getString("calle"));
        domicilio.setNumero(rs.getInt("numero"));
        domicilio.setCiudad(rs.getString("ciudad"));
        domicilio.setProvincia(Provincia.fromLabel(rs.getString("provincia")));
        domicilio.setCodigoPostal(rs.getString("codigoPostal"));
        domicilio.setPais(Pais.fromLabel(rs.getString("pais")));
        domicilio.setEliminado(rs.getBoolean("df_elim"));
    }

    @Override
    public void insertar(Empresa entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);) {
            setStatementParameters(stmt, entidad);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getInt(1));
                    System.out.println("Empresa insertada con ID: " + entidad.getId());
                } else {
                    throw new SQLException("La inserción de la empresa falló, no se obtuvo ID generado ");
                }
            }
        }
    }

    @Override
    public void actualizar(Empresa entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL);) {
            setStatementParameters(stmt, entidad);
            stmt.setInt(6, entidad.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(DELETE_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer eliminación lógica de la empresa con ID " + id + e.getMessage());
        }
    }

    @Override
    public void recuperar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(RESET_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo deshacer la eliminación lógica de la empresa con ID " + id + " " + e.getMessage());
        }
    }

    public Empresa getById(int id, Boolean getDeleted) throws Exception {
        System.out.println("ID para busqueda:" + id);
        String sql = SELECT_BY_ID_SQL;
        if (getDeleted == false) {
            sql += " AND e.eliminado = FALSE";
        }
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Empresa empresa = new Empresa();
                setEmpresa(rs, empresa);
                int domicilioFiscalId = rs.getInt("domicilioFiscalId");
                if (domicilioFiscalId > 0) {
                    DomicilioFiscal domicilio = new DomicilioFiscal();
                    setDomicilio(rs, domicilio);
                    empresa.setDomicilioFiscal(domicilio);
                } else {
                    empresa.setDomicilioFiscal(null);
                }
                return empresa;

            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener empresa por ID " + e.getMessage());
        }
        return null;
    }

    @Override
    public Empresa getById(int id) throws Exception {
        return getById(id, false);
    }

    @Override
    public List<Empresa> getAll() throws Exception {
        List<Empresa> empresas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL); ResultSet rs = stmt.executeQuery();) {
            while (rs.next()) {
                Empresa empresa = new Empresa();
                setEmpresa(rs, empresa);
                int domicilioFiscalId = rs.getInt("domicilioFiscalId");
                if (domicilioFiscalId > 0) {
                    DomicilioFiscal domicilio = new DomicilioFiscal();
                    setDomicilio(rs, domicilio);
                    empresa.setDomicilioFiscal(domicilio);
                } else {
                    empresa.setDomicilioFiscal(null);
                }
                empresas.add(empresa);

            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener todas las empresas " + e.getMessage());
        }
        return empresas;
    }

    @Override
    public void insertarTx(Empresa entidad, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);) {
            setStatementParameters(stmt, entidad);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entidad.setId(generatedKeys.getInt(1));
                    System.out.println("Empresa insertada con ID: " + entidad.getId());
                } else {
                    throw new SQLException("La inserción de la empresa en transacción falló, no se obtuvo ID generado ");
                }
            }
        }
    }

    @Override
    public void actualizarTx(Empresa entidad, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL);) {
            setStatementParameters(stmt, entidad);
            stmt.setInt(6, entidad.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer actualización en transacción de la empresa " + e.getMessage());
        }
    }

    @Override
    public void eliminarTx(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo hacer eliminación lógica en transacción de la empresa con ID " + id + e.getMessage());
        }
    }

    @Override
    public void recuperarTx(int id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(RESET_SQL);) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("No se pudo deshacer la eliminación lógica en transacción de la empresa con ID " + id + " " + e.getMessage());
        }
    }

    public List<Empresa> getByRazonSocial(String razonSocial) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_RAZON_SOCIAL_SQL);) {
            stmt.setString(1, "%" + razonSocial + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empresa empresa = new Empresa();
                setEmpresa(rs, empresa);
                int domicilioFiscalId = rs.getInt("domicilioFiscalId");
                if (domicilioFiscalId > 0) {
                    DomicilioFiscal domicilio = new DomicilioFiscal();
                    setDomicilio(rs, domicilio);
                    empresa.setDomicilioFiscal(domicilio);
                } else {
                    empresa.setDomicilioFiscal(null);
                }
                empresas.add(empresa);

            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener las empresas filtradas por razón social " + e.getMessage());
        }
        return empresas;
    }

    public Empresa getByCuit(String cuit, Boolean getDeleted) throws SQLException {
        String sql = SEARCH_BY_CUIT_SQL;
        if (getDeleted == false) {
            sql += " AND e.eliminado = FALSE";
        }
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, cuit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empresa empresa = new Empresa();
                setEmpresa(rs, empresa);
                int domicilioFiscalId = rs.getInt("domicilioFiscalId");
                if (domicilioFiscalId > 0) {
                    DomicilioFiscal domicilio = new DomicilioFiscal();
                    setDomicilio(rs, domicilio);
                    empresa.setDomicilioFiscal(domicilio);
                } else {
                    empresa.setDomicilioFiscal(null);
                }
                return empresa;

            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener la empresa por cuit " + cuit + " " + e.getMessage());
        }
        return null;
    }

    public Empresa getByCuit(String cuit) throws Exception {
        return getByCuit(cuit, false);
    }

}
