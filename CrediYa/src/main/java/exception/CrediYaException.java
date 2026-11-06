package exception;

/**
 * CrediYaException  (se completa en la Fase 4)
 *
 * Error propio del sistema. Se lanza (throw) cuando algo no se puede hacer:
 * abono mayor al saldo, documento repetido, MySQL apagado, etc.
 *
 * Usa:      Exception (clase de Java de la que hereda)
 * La usan:  Prestamo, todos los DAO, ArchivoReportes, todos los Controller (la atrapan con catch)
 */
public class CrediYaException extends Exception {

}
