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
import model.EstadoPrestamo;
import model.Prestamo;

public class PrestamoDao implements IConsultaDao<Prestamo> {

    private final Conexion c = Conexion.getInstance();
    private final ClienteDao clienteDao = new ClienteDao();
    private final EmpleadoDao empleadoDao = new EmpleadoDao();

    private Prestamo crearDesdeFila(ResultSet rs) throws SQLException, CrediYaException {
        return new Prestamo(rs.getLong("id"),
                clienteDao.buscar(rs.getLong("cliente_id")),
                empleadoDao.buscar(rs.getLong("empleado_id")),
                rs.getDouble("monto"), rs.getDouble("interes"), rs.getInt("cuotas"),
                rs.getDate("fecha_inicio").toLocalDate(),
                EstadoPrestamo.valueOf(rs.getString("estado")),
                rs.getDouble("saldo"));
    }

    public Prestamo agregar(Prestamo prestamo) throws CrediYaException {
        String sql = """
                     insert into prestamos(cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado, saldo)
                     values (?,?,?,?,?,?,?,?)
                     """;
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, prestamo.getCliente().getId());
            ps.setLong(2, prestamo.getEmpleado().getId());
            ps.setDouble(3, prestamo.getMonto());
            ps.setDouble(4, prestamo.getInteres());
            ps.setInt(5, prestamo.getCuotas());
            ps.setDate(6, Date.valueOf(prestamo.getFechaInicio()));
            ps.setString(7, prestamo.getEstado().name());
            ps.setDouble(8, prestamo.getSaldo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    prestamo.setId(rs.getLong(1));
                    return prestamo;
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al registrar el prestamo: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarEstadoYSaldo(Prestamo prestamo) throws CrediYaException {
        String sql = "update prestamos set estado=?, saldo=? where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prestamo.getEstado().name());
            ps.setDouble(2, prestamo.getSaldo());
            ps.setLong(3, prestamo.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new CrediYaException("Error al actualizar el prestamo: " + e.getMessage());
        }
    }

    @Override
    public Prestamo buscar(long id) throws CrediYaException {
        String sql = "select * from prestamos where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el prestamo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Prestamo> listar() throws CrediYaException {
        String sql = "select * from prestamos";
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                prestamos.add(crearDesdeFila(rs));
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al listar prestamos: " + e.getMessage());
        }
        return prestamos;
    }
}
