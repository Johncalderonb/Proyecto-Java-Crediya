package dao;

import exception.CrediYaException;

public interface ICrudDao<T> extends IConsultaDao<T> {

    T agregar(T entidad) throws CrediYaException;

    boolean actualizar(T entidad) throws CrediYaException;

    boolean eliminar(T entidad) throws CrediYaException;
}
