/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import java.util.List;
import java.sql.Connection;

/**
 *
 * @author eugeniavogt
 * Métodos comunes a las diferentes entidades
 */
public interface GenericDAO<T> {

    void insertar(T entidad) throws Exception;
    void actualizar(T entidad) throws Exception;
    void eliminar(int id) throws Exception;
    void recuperar(int id) throws Exception;
    T getById(int id) throws Exception;
    List<T> getAll() throws Exception;
    void insertarTx(T entity, Connection conn) throws Exception;
    void actualizarTx(T entity, Connection conn) throws Exception;
    void eliminarTx(int id, Connection conn) throws Exception;
    void recuperarTx(int id, Connection conn) throws Exception;

}
