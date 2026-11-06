package dao;

import model.Prestamo;

/**
 * PrestamoDao  (se completa en la Fase 5)
 *
 * SQL de la tabla prestamos: agregar, buscar, listar y actualizar estado/saldo.
 * No se borran prestamos, por eso solo firma IConsultaDao.
 *
 * Usa:      IConsultaDao (contrato), Conexion, ClienteDao, EmpleadoDao, Prestamo, EstadoPrestamo
 * La usan:  PrestamoController, ClienteController, PagoController,
 *            PortalClienteController, InformeController
 */
public class PrestamoDao implements IConsultaDao<Prestamo> {

}
