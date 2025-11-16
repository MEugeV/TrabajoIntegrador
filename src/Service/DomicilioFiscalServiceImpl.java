/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.GenericDAO;
import Models.DomicilioFiscal;
import java.util.List;

/**
 *
 * @author eugeniavogt
 */
public class DomicilioFiscalServiceImpl implements GenericService<DomicilioFiscal> {

    private final GenericDAO<DomicilioFiscal> domicilioFiscalDAO;

    public DomicilioFiscalServiceImpl(GenericDAO<DomicilioFiscal> domicilioFiscalDAO) {
        this.domicilioFiscalDAO = domicilioFiscalDAO;
    }

    /**
     * Aplica validaciones de dominio sobre la entidad Domicilio Fiscal
     * Verifica campos obligatorios y formato de datos
     */
    public void validateDomicilioFiscal(DomicilioFiscal entidad) throws Exception {
        if (entidad.getCalle() == null || entidad.getCalle().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la calle no puede ser nulo");
        }

        Integer numero = entidad.getNumero();
        if (numero != null && numero < 0) {
            throw new IllegalArgumentException("El número debe ser mayor o igual a cero o nulo");
        }

        if (entidad.getCiudad() == null || entidad.getCiudad().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser nulo");
        }

        if (entidad.getProvincia() == null) {
            throw new IllegalArgumentException("El nombre de la provincia no puede ser nulo");
        }

        if (entidad.getPais() == null) {
            throw new IllegalArgumentException("El nombre del pais no puede ser nulo");
        }

    }

    @Override
    public void insertar(DomicilioFiscal entidad) throws Exception {
        validateDomicilioFiscal(entidad);
        domicilioFiscalDAO.insertar(entidad);
    }

    @Override
    public void actualizar(DomicilioFiscal entidad) throws Exception {
        validateDomicilioFiscal(entidad);
        domicilioFiscalDAO.actualizar(entidad);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de eliminación de domicilio inválido: debe ser mayor que cero");
        }
        domicilioFiscalDAO.eliminar(id);
    }

    @Override
    public void recuperar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de recuperación de domicilio inválido: debe ser mayor que cero");
        }
        domicilioFiscalDAO.recuperar(id);
    }

    @Override
    public DomicilioFiscal getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de búsqueda de domicilio inválido: debe ser mayor que cero");
        }
        return domicilioFiscalDAO.getById(id);
    }

    @Override
    public List<DomicilioFiscal> getAll() throws Exception {
        return domicilioFiscalDAO.getAll();
    }

}
