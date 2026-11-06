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
import model.Cliente;

public class ClienteDao implements ICrudDao<Cliente> {

    private final Conexion c = Conexion.getInstance();

    private Cliente crearDesdeFila(ResultSet rs) throws SQLException {
        return new Cliente(rs.getLong("id"), rs.getString("nombre"), rs.getString("documento"),
                rs.getString("correo"), rs.getString("telefono"), rs.getString("clave"));
    }

    @Override
    public Cliente agregar(Cliente cliente) throws CrediYaException {
        String sql = "insert into clientes(nombre, documento, correo, telefono, clave) values (?,?,?,?,?)";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getClave());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                    return cliente;
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CrediYaException("Ya existe un cliente con el documento " + cliente.getDocumento());
        } catch (SQLException e) {
            throw new CrediYaException("Error al registrar el cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizar(Cliente cliente) throws CrediYaException {
        String sql = "update clientes set nombre=?, documento=?, correo=?, telefono=?, clave=? where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getClave());
            ps.setLong(6, cliente.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new CrediYaException("Error al actualizar el cliente: " + e.getMessage());
        }
    }

    @Override
    public boolean eliminar(Cliente cliente) throws CrediYaException {
        String sql = "delete from clientes where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, cliente.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new CrediYaException("No se puede eliminar: el cliente tiene prestamos asociados");
        } catch (SQLException e) {
            throw new CrediYaException("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente buscar(long id) throws CrediYaException {
        String sql = "select * from clientes where id=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el cliente: " + e.getMessage());
        }
        return null;
    }

    public Cliente buscarPorDocumento(String documento) throws CrediYaException {
        String sql = "select * from clientes where documento=?";
        try (Connection con = c.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return crearDesdeFila(rs);
                }
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al buscar el cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> listar() throws CrediYaException {
        String sql = "select * from clientes";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = c.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(crearDesdeFila(rs));
            }
        } catch (SQLException e) {
            throw new CrediYaException("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
}
