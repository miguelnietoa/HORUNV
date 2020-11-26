package database;

import model.*;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class DatabaseManager {

    private static Connection conn = null;
    private static PreparedStatement psLinkCourses;
    private static PreparedStatement psCourseProfessor;
    private static PreparedStatement psScheduleCourse;

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            return getConnection("181.130.217.56", "horunv", "sa123456");
        } else {
            return conn;
        }
    }

    private static Connection getConnection(String address, String user, String pass) {
        try {
            OracleDataSource ds = new OracleDataSource();
            ds.setServerName(address);
            ds.setPortNumber(1521);
            ds.setDriverType("thin");
            ds.setDatabaseName("XE");
            ds.setUser(user);
            ds.setPassword(pass);
            conn = ds.getConnection();

            psLinkCourses = conn.prepareStatement(
                    "SELECT \"nrc\", \"cupos_totales\", \"modalidad\"  " +
                            "FROM \"Curso\"" +
                            " WHERE \"cod_asig\" = ?"
            );
            psCourseProfessor = conn.prepareStatement(
                    "SELECT * FROM \"Docente\" " +
                            "WHERE \"codigo\" IN " +
                            "(SELECT \"cod_doc\" " +
                            "FROM \"DocenteDictaCurso\" " +
                            "WHERE \"nrc_curso\" = ?)"
            );
            psScheduleCourse = conn.prepareStatement(
                    "SELECT * FROM \"Horario\" WHERE \"nrc_curso\" = ?"
            );

            return conn;
        } catch (SQLException error) {
            System.out.println("Error en la conexión con la BD: " + error);
        }
        return null;
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
                User.setUsername(rs.getString(1));
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
            User.setCodeUser(codeUser);
            User.setFullname(fullname.toString());
            User.setGender(rs.getString(5).charAt(0));
            User.setIdPlan(rs.getInt(6));
            User.setPeriod(rs.getString(7));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Subject> getProjection(int codeStudent) {
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
            HashMap<String, Subject> projection = new HashMap<>();
            while (rs.next()) {
                Subject sub = new Subject(rs.getString(1), rs.getString(2), rs.getInt(3));
                sub.setCourses(linkCourses(sub));
                projection.put(rs.getString(1), sub);
            }
            return projection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HashMap<Integer, Course> linkCourses(Subject subject) {
        ResultSet rs;
        try {

            psLinkCourses.setString(1, subject.getCode());

            rs = psLinkCourses.executeQuery();
            HashMap<Integer, Course> courses = new HashMap<>();
            while (rs.next()) {
                ResultSet rs1;
                psCourseProfessor.setInt(1, rs.getInt(1));
                rs1 = psCourseProfessor.executeQuery();
                Professor professor = null;
                if (rs1.next()) {
                    professor = new Professor(
                            rs1.getString(1),
                            rs1.getString(2) + " " +
                                    (rs1.getString(3) == null ? "" : rs1.getString(3) + " ") +
                                    rs1.getString(4) + " " + rs1.getString(5)
                    );
                } else {
                    System.err.println("Error: Course " + rs.getInt(1) + " does not have professors.");
                }
                Course c = new Course(subject, rs.getInt(1), rs.getString(3).charAt(0), rs.getInt(2), professor);
                courses.put(c.getNrc(), c);
                c.setSchedules(getSchedule(c.getNrc()));
            }
            return courses;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private static LinkedList<Schedule> getSchedule(int nrc) {
        ResultSet rs;
        LinkedList<Schedule> schedules = new LinkedList<>();
        try {
            psScheduleCourse.setInt(1, nrc);
            rs = psScheduleCourse.executeQuery();

            while (rs.next()) {
                Schedule s = new Schedule(rs.getString(3).charAt(0), rs.getInt(4), rs.getInt(5), rs.getString(2));
                schedules.add(s);
            }
            return schedules;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * calculate the schedule <code>index</code>
     * and set it to User.currentCourses
     * taking each subjectCode from User.selectedSubjects
     *
     * @param index
     */
    public static void setSchedule(int index) {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        int size = User.getSelectedSubjects().size();
        if (!User.getSelectedSubjects().isEmpty()) {
            Subject last = User.getSelectedSubjects().getLast();
            for (Subject selectedSubject : User.getSelectedSubjects()) {
                query.append("(SELECT \"nrc\" FROM \"Curso\" WHERE \"cod_asig\" = '").append(selectedSubject.getCode()).append("')\n");
                if (!selectedSubject.equals(last)) {
                    query.append("CROSS JOIN\n");
                }
            }
            query.append("OFFSET ").append(index).append(" ROWS FETCH NEXT 1 ROWS ONLY");

            ResultSet rs;
            try {
                rs = conn.createStatement().executeQuery(String.valueOf(query));
                if (rs.next()) {
                    User.getCurrentCourses().clear();
                    for (int i = 0; i < size; i++) {
                        Course course = User.getSelectedSubjects().get(i).getCourses().get(rs.getInt(i + 1));
                        User.getCurrentCourses().add(course);
                    }
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static void cantGeneratedSchedules() {
        StringBuilder query = new StringBuilder("SELECT count(*) AS \"count\" FROM ");
        int size = User.getSelectedSubjects().size();
        if (!User.getSelectedSubjects().isEmpty()) {
            Subject last = User.getSelectedSubjects().getLast();
            for (Subject selectedSubject : User.getSelectedSubjects()) {
                query.append("(SELECT \"nrc\" FROM \"Curso\" WHERE \"cod_asig\" = '").append(selectedSubject.getCode()).append("')\n");
                if (!selectedSubject.equals(last)) {
                    query.append("CROSS JOIN\n");
                }
            }
            ResultSet rs;
            try {
                rs = conn.createStatement().executeQuery(String.valueOf(query));
                if (rs.next()) {
                    User.setCantGeneratedSchedules(rs.getInt("count"));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            User.setCantGeneratedSchedules(0);
        }
    }


}