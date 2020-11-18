package database;

import model.Subject;
import oracle.jdbc.pool.OracleDataSource;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class DatabaseManager {

    private static Connection conn = null;

    private DatabaseManager() {}

    public static Connection getConnection(String address, String user, String pass) {
        if (conn != null) {
            return conn;
        } else {
            try {
                OracleDataSource ds = new OracleDataSource();
                ds.setServerName(address);
                ds.setPortNumber(1521);
                ds.setDriverType("thin");
                ds.setDatabaseName("XE");
                ds.setUser(user);
                ds.setPassword(pass);
                return conn = ds.getConnection();
            } catch (SQLException error) {
                System.out.println("Error en la conexión con la BD: " + error);
            }
            return null;
        }
    }

    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean isLogged(String user, String pass) {
        ResultSet rs;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM \"Credencial\" WHERE \"usuario\" = ? AND \"pass\" = ?"
            );
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                Student.setUsername(rs.getString(1));
                getInfoUser(rs.getInt(3));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getInfoUser(int codeUser) {
        ResultSet rs;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT \"nombre1\", \"nombre2\", \"apellido1\", \"apellido2\", \"sexo\", " +
                    "\"id_plan_estudio\", \"periodo_inscrito\" " +
                    "FROM \"Estudiante\" WHERE \"codigo\" = ?"
            );
            ps.setInt(1, codeUser);
            rs = ps.executeQuery();
            rs.next();
            StringBuilder fullname = new StringBuilder();
            for (int i = 1; i <= 4; i++) {
                if (rs.getString(i) == null)
                    continue;
                fullname.append(rs.getString(i));
                if (i != 4) {
                    fullname.append(" ");
                }
            }
            Student.setCodeUser(codeUser);
            Student.setFullname(fullname.toString());
            Student.setGender(rs.getString(5).charAt(0));
            Student.setIdPlan(rs.getInt(6));
            Student.setPeriod(rs.getString(7));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LinkedList<Subject> getProjection(int codeStudent) {
        ResultSet rs;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM \"Asignatura\"\n" +
                    "WHERE \"codigo\" IN(\n" +
                    "SELECT \"cod_asig\" \n" +
                    "FROM \"Proyeccion\" \n" +
                    "WHERE \"cod_estu\" = ?)"
            );
            ps.setInt(1, codeStudent);
            rs = ps.executeQuery();
            LinkedList<Subject> projection = new LinkedList<Subject>();
            while (rs.next()) {
                projection.add(new Subject(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }
            return projection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}