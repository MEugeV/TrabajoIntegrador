/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.EmpresaDAO;
import Dao.DomicilioFiscalDAO;
import Models.Empresa;
import Models.DomicilioFiscal;
import java.util.List;
import Config.DatabaseConnection;
import java.sql.Connection;

/**
 *
 * @author eugeniavogt
 * Capa intermedia entre la UI y el DAO que implementa las validaciones de negocio
 * Una empresa puede tener un sólo Domicilio Fiscal y un Domicilio Fiscal puede pertenecer a una sóla empresa.
 */
public class EmpresaServiceImpl implements GenericService<Empresa> {
    // acceso a capa de persistencia
    private final EmpresaDAO empresaDAO;
    private final DomicilioFiscalDAO domicilioFiscalDAO;

    public EmpresaServiceImpl(EmpresaDAO empresaDAO, DomicilioFiscalDAO domicilioFiscalDAO) {
        this.empresaDAO = empresaDAO;
        this.domicilioFiscalDAO = domicilioFiscalDAO;
    }

    /**
     * Aplica validaciones de dominio sobre la entidad Empresa. 
     * Verifica campos obligatorios y formato de datos (CUIT, email)
     */
    public void validateEmpresa(Empresa entidad) throws Exception {
        if (entidad.getRazonSocial() == null || entidad.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la razón social no puede ser nulo");
        }

        if (entidad.getCuit() == null || entidad.getCuit().trim().isEmpty()) {
            throw new IllegalArgumentException("El cuit no puede ser nulo");
        }

        if (entidad.getActividadPrincipal() == null) {
            throw new IllegalArgumentException("La actividad principal no puede ser nula");
        }

        if (entidad.getEmail() == null || entidad.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }

        if (!entidad.getCuit().matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("El cuit debe contener 11 cifras numéricas");
        }

        if (!entidad.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El email no es válido");
        }
    }

    @Override
    public void insertar(Empresa entidad) throws Exception {
        validateEmpresa(entidad);
        empresaDAO.insertar(entidad);
    }

    @Override
    public void actualizar(Empresa entidad) throws Exception {
        validateEmpresa(entidad);
        empresaDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de eliminación de empresa inválido: debe ser mayor que cero");
        }
        empresaDAO.eliminar(id);
    }

    @Override
    public void recuperar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de recuperación de empresa inválido: debe ser mayor que cero");
        }
        empresaDAO.recuperar(id);
    }

    public Empresa getById(int id, Boolean getDeleted) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de búsqueda de empresa inválido: debe ser mayor que cero");
        }
        return empresaDAO.getById(id, getDeleted);
    }

    @Override
    public Empresa getById(int id) throws Exception {
        return getById(id, false);
    }

    @Override
    public List<Empresa> getAll() throws Exception {
        return empresaDAO.getAll();
    }

    public List<Empresa> getByRazonSocial(String razonSocial) throws Exception {
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la razón social no puede ser nulo");
        }
        return empresaDAO.getByRazonSocial(razonSocial);
    }

    public Empresa getByCuit(String cuit, Boolean getDeleted) throws Exception {
        if (cuit == null || !cuit.matches("^[0-9]{11}$")) {
            throw new IllegalArgumentException("El cuit debe contener 11 cifras numéricas");
        }
        return empresaDAO.getByCuit(cuit, getDeleted);
    }

    public Empresa getByCuit(String cuit) throws Exception {
        return getByCuit(cuit, false);
    }

    public void insertarConDomicilio(Empresa empresa, DomicilioFiscal domicilio) throws Exception {
        DomicilioFiscalServiceImpl domicilioService = new DomicilioFiscalServiceImpl(new DomicilioFiscalDAO());
        domicilioService.validateDomicilioFiscal(domicilio);
        validateEmpresa(empresa);

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            domicilioFiscalDAO.insertarTx(domicilio, conn);

            empresa.setDomicilioFiscal(domicilio);

            empresaDAO.insertarTx(empresa, conn);

            conn.commit();
            System.out.println("Transacción de inserción completada exitosamente.");

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Error al insertar empresa con domicilio: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void actualizarConDomicilio(Empresa empresa, DomicilioFiscal domicilio) throws Exception {
        DomicilioFiscalServiceImpl domicilioService = new DomicilioFiscalServiceImpl(new DomicilioFiscalDAO());
        domicilioService.validateDomicilioFiscal(domicilio);
        validateEmpresa(empresa);

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            domicilioFiscalDAO.actualizarTx(domicilio, conn);

            empresaDAO.actualizarTx(empresa, conn);

            conn.commit();
            System.out.println("Transacción de actualización completada exitosamente.");

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Error al actualizar empresa con domicilio: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public void eliminarConDomicilio(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de eliminación de empresa inválido: debe ser mayor que cero");
        }

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empresa empresa = getById(id);
            if (empresa.isEliminado()) {
                throw new IllegalArgumentException("Error: La empresa a eliminar ya está eliminada");
            }
            DomicilioFiscal domicilioFiscal = empresa.getDomicilioFiscal();
            int domicilioFiscalId;
            if (domicilioFiscal != null) {
                domicilioFiscalId = domicilioFiscal.getId();
                domicilioFiscalDAO.eliminarTx(domicilioFiscalId, conn);
            }

            empresaDAO.eliminarTx(id, conn);

            conn.commit();
            System.out.println("Transacción de eliminación completada exitosamente.");

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Error al eliminar empresa con domicilio: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

    }

    public void recuperarConDomicilio(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de recuperación de empresa inválido: debe ser mayor que cero");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            Empresa empresa = getById(id, true);
            if (!empresa.isEliminado()) {
                throw new IllegalArgumentException("Error: La empresa a recuperar no está eliminada");
            }
            DomicilioFiscal domicilioFiscal = empresa.getDomicilioFiscal();
            int domicilioFiscalId;
            if (domicilioFiscal != null) {
                domicilioFiscalId = domicilioFiscal.getId();
                domicilioFiscalDAO.recuperarTx(domicilioFiscalId, conn);
            }

            empresaDAO.recuperarTx(id, conn);

            conn.commit();
            System.out.println("Transacción de recuperación completada exitosamente.");

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception("Error al recuperar empresa con domicilio: " + e.getMessage(), e);

        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

}
