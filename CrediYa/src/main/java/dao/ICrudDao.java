package dao;

/**
 * ICrudDao  (se completa en la Fase 5)
 *
 * CONTRATO completo: buscar y listar (heredados) + agregar, actualizar y eliminar.
 *
 * Usa:      IConsultaDao (lo extiende), CrediYaException
 * La usan:  EmpleadoDao, ClienteDao (lo implementan)
 */
public interface ICrudDao<T> extends IConsultaDao<T> {

}
