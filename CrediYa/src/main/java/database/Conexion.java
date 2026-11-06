package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;

    private final String url = "jdbc:mysql://localhost:3306/crediya_db";
    private final String user = "root";
    private final String password = "crediya123";

    private Conexion() {
    }

    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
