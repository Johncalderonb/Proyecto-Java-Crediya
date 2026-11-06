package dao;

/**
 * IConsultaDao  (se completa en la Fase 5)
 *
 * CONTRATO de consulta: toda clase que lo firme debe tener buscar(id) y listar().
 * <T> = el tipo de objeto (Empleado, Cliente, Prestamo, Pago).
 *
 * Usa:      CrediYaException
 * La usan:  ICrudDao (lo amplia), PrestamoDao, PagoDao (lo implementan)
 */
public interface IConsultaDao<T> {

}
