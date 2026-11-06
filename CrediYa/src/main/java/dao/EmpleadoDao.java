package dao;

import database.Conexion;
import exception.CrediYaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Empleado;
import model.RolEmpleado;

public class EmpleadoDao implements ICrudDao<Empleado> {

    private final Conexion c = Conexion.getInstance();

    private Empleado crearDesdeFila(ResultSet rs) throws SQLException {
        return new Empleado(rs.getLong("id"), rs.getString("nombre"), rs.getString("documento"),
                RolEmpleado.valueOf(rs.getString("rol")), rs.getString("correo"), rs.getDouble("salario"), rs.getString("clave"));
    }

    @Override
    public Empleado agregar(Empleado empleado) throws CrediYaException {
        String sql = "insert into empleados(nombre, documento, rol, correo, salario, clave) values (?,?,?,?,?,?)";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDocumento());
            ps.setString(3, empleado.getRol().name());
            ps.setString(4, empleado.getCorreo());
            ps.setDouble(5, empleado.getSalario());
            ps.setString(6, empleado.getClave());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setId(rs.getLong(1));
                    return empleado;
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CrediYaException("Ya existe un empleado con el documento " + empleado.getDocumento());
        } catch (SQLException e) {
            throw new CrediYaException("Error al registrar el empleado: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizar(Empleado empleado) throws CrediYaException {
        String sql = "update empleados set nombre=?, documento=?, rol=?, correo=?, salario=?, clave=? where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDocumento());
            ps.setString(3, empleado.getRol().name());
            ps.setString(4, empleado.getCorreo());
            ps.setDouble(5, empleado.getSalario());
            ps.setString(6, empleado.getClave());
            ps.setLong(7, empleado.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new CrediYaException("Error al actualizar el empleado: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminar(Empleado empleado) throws CrediYaException {
        String sql = "delete from empleados where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, empleado.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CrediYaException("No se puede eliminar: el empleado tiene prestamos asociados");
        } catch (SQLException e) {
            throw new CrediYaException("Error al eliminar el empleado: " + e.getMessage());
        }
    }

    @Override
    public Empleado buscar(long id) throws CrediYaException {
        String sql = "select * from empleados where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el empleado: " + e.getMessage());
        }
        return null;
    }

    public Empleado buscarPorDocumento(String documento) throws CrediYaException {
        String sql = "select * from empleados where documento=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el empleado: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Empleado> listar() throws CrediYaException {
        String sql = "select * from empleados";
        List<Empleado> empleados = new ArrayList<>();
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                empleados.add(crearDesdeFila(rs));
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al listar empleados: " + e.getMessage());
        }
        return empleados;
    }
}
