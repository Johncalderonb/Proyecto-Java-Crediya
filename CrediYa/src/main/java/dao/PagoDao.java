package dao;

import model.Pago;

/**
 * PagoDao  (se completa en la Fase 5)
 *
 * SQL de la tabla pagos. Registra el pago y actualiza el saldo del prestamo
 * en una TRANSACCION (todo o nada). Los pagos no se editan ni se borran.
 *
 * Usa:      IConsultaDao (contrato), Conexion, Pago, Prestamo
 * La usan:  PagoController, PortalClienteController
 */
public class PagoDao implements IConsultaDao<Pago> {

}
