package dao;

import exception.CrediYaException;
import java.util.List;

public interface IConsultaDao<T> {

    T buscar(long id) throws CrediYaException;

    List<T> listar() throws CrediYaException;
}
