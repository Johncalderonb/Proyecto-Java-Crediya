package dao;

import database.Conexion;
import exception.CrediYaException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Pago;
import model.Prestamo;

public class PagoDao implements IConsultaDao<Pago> {

    private final Conexion c = Conexion.getInstance();

    private Pago crearDesdeFila(ResultSet rs) throws SQLException {
        return new Pago(rs.getLong("id"), rs.getLong("prestamo_id"),
                rs.getDate("fecha_pago").toLocalDate(), rs.getDouble("monto"));
    }

    public Pago registrar(Pago pago, Prestamo prestamo) throws CrediYaException {
        String sqlPago = "insert into pagos(prestamo_id, fecha_pago, monto) values (?,?,?)";
        String sqlPrestamo = "update prestamos set saldo=?, estado=? where id=?";
        try (Connection con = c.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement psPago = con.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement psPrestamo = con.prepareStatement(sqlPrestamo)) {
                psPago.setLong(1, pago.getPrestamoId());
                psPago.setDate(2, Date.valueOf(pago.getFechaPago()));
                psPago.setDouble(3, pago.getMonto());
                psPago.executeUpdate();

                psPrestamo.setDouble(1, prestamo.getSaldo());
                psPrestamo.setString(2, prestamo.getEstado().name());
                psPrestamo.setLong(3, prestamo.getId());
                psPrestamo.executeUpdate();

                con.commit();
                try (ResultSet rs = psPago.getGeneratedKeys()) {
                    if (rs.next()) {
                        pago.setId(rs.getLong(1));
                    }
                }
                return pago;
            } catch (SQLException e) {
                con.rollback();
                throw new CrediYaException("No se registro el pago, se deshicieron los cambios: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error de conexion al registrar el pago: " + e.getMessage());
        }
    }

    public List<Pago> listarPorPrestamo(long prestamoId) throws CrediYaException {
        String sql = "select * from pagos where prestamo_id=? order by fecha_pago";
        List<Pago> pagos = new ArrayList<>();
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagos.add(crearDesdeFila(rs));
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al listar los pagos: " + e.getMessage());
        }
        return pagos;
    }

    @Override
    public Pago buscar(long id) throws CrediYaException {
        String sql = "select * from pagos where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el pago: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pago> listar() throws CrediYaException {
        String sql = "select * from pagos order by fecha_pago";
        List<Pago> pagos = new ArrayList<>();
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                pagos.add(crearDesdeFila(rs));
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al listar los pagos: " + e.getMessage());
        }
        return pagos;
    }
}
