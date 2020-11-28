package database;

import model.*;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class DatabaseManager {

    private static Connection conn = null;
    private static PreparedStatement psLinkCourses;
    private static PreparedStatement psCourseProfessor;
    private static PreparedStatement psScheduleCourse;
    private static PreparedStatement psSavedSchedule;
    private static PreparedStatement psGetMaxConse;
    private static PreparedStatement psSavedScheduleCourses;
    private static PreparedStatement psCheckScheduleName;
    private static PreparedStatement psGetSavedSchedule;
    private DatabaseManager() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            //Ip ImiServer: 181.130.217.56
            //return getConnection("localhost", "horunv", "sa123456");
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

            psSavedSchedule = conn.prepareStatement(
                "INSERT INTO \"PosibleHorarioTieneCurso\" (\"cod_estu\", \"consecutivo\", \"nrc_curso\") VALUES (?, ?, ?)"
            );
            psGetMaxConse = conn.prepareStatement(
                "SELECT MAX(\"consecutivo\") AS \"max\" FROM \"PosibleHorario\" WHERE \"cod_estu\" = ?"
            );
            psSavedScheduleCourses = conn.prepareStatement(
                "INSERT INTO \"PosibleHorario\" (\"cod_estu\",\"consecutivo\",\"nombre\") " +
                "VALUES ( ? , ? , ? )");
            psCheckScheduleName = conn.prepareStatement(
                "SELECT COUNT(*) FROM \"PosibleHorario\" WHERE \"cod_estu\" = ? AND \"nombre\" = ?");
            psSavedSchedule = conn.prepareStatement(
                    "SELECT * FROM \"PosibleHorario\" WHERE \"cod_estu\" = ?"
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
        String query = "SELECT \"nombre1\", \"nombre2\", \"apellido1\", \"apellido2\", \"sexo\", " +
                "\"id_plan_estudio\", \"periodo_inscrito\" " +
                "FROM \"Estudiante\" WHERE \"codigo\" = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
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
                    if (!courses.isEmpty()) {
                        for (int i = 0; i < courses.values().size(); i++) {
                            Course c = (Course) courses.values().toArray()[i];
                            if (c.getProfessor().getId().equals(rs1.getString(1))) {
                                professor = c.getProfessor();
                                break;
                            }
                        }
                    }
                    if (professor == null) {
                        professor = new Professor(
                                rs1.getString(1),
                                rs1.getString(2) + " " +
                                        (rs1.getString(3) == null ? "" : rs1.getString(3) + " ") +
                                        rs1.getString(4) + " " + rs1.getString(5)
                        );
                    }
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
        ResultSet rs;
        String query = getQuerySchedule(index);
        if (query != null) {
            try {
                rs = conn.createStatement().executeQuery(query);
                User.getCurrentCourses().clear();
                if (rs.next()) {
                    for (int i = 0; i < User.getSelectedSubjects().size(); i++) {
                        Course course = User.getSelectedSubjects().get(i).getCourses().get(rs.getInt(i + 1));
                        User.getCurrentCourses().add(course);
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            User.getCurrentCourses().clear();
        }
    }


    public static void cantGeneratedSchedules() {
        String query = getQuerySchedule(User.getActiveIndexSchedule());
        if (query != null) {
            query = query.replaceFirst("\\*", "count(*) AS \"count\"");
            ResultSet rs;
            if (!User.getSelectedSubjects().isEmpty()) {
                try {
                    rs = conn.createStatement().executeQuery(query);
                    if (rs.next()) {
                        User.setCantGeneratedSchedules(rs.getInt("count"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else{
                User.setCantGeneratedSchedules(0);
            }
        }else{
            User.setCantGeneratedSchedules(0);
        }
    }

    private static int getConsecutive() {
        int r = 0;
        try {
            psGetMaxConse.setInt(1, User.getCodeUser());
            ResultSet rs = psGetMaxConse.executeQuery();
            if (rs.next()) {
                r = rs.getInt("max");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r;
    }

    public static boolean thereIsSavedScheduleWithName(String name) {
        try {
            psCheckScheduleName.setInt(1, User.getCodeUser());
            psCheckScheduleName.setString(2, name);
            ResultSet rs = psCheckScheduleName.executeQuery();
            rs.next();
            return rs.getInt(1) != 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void addSavedSchedule(String name) {
        int con = getConsecutive() + 1;
        if (User.getCurrentCourses().size() > 0) {
            try {
                psSavedScheduleCourses.setInt(1, User.getCodeUser());
                psSavedScheduleCourses.setInt(2, con);
                psSavedScheduleCourses.setString(3, name);
                psSavedScheduleCourses.executeQuery();
                for (Course course : User.getCurrentCourses()) {
                    psSavedSchedule.setInt(1, User.getCodeUser());
                    psSavedSchedule.setInt(2, con);
                    psSavedSchedule.setInt(3, course.getNrc());
                    psSavedSchedule.executeQuery();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private static String getQuerySchedule(int index) {
        // Filters by selection
        String filterQuery = "";
        if (!User.getFilters().isEmpty()) {
            filterQuery = "AND \"nrc\" NOT IN (SELECT \"nrc_curso\" FROM \"Horario\" WHERE ";
            boolean first = true;
            for (Object[] filter : User.getFilters()) {
                int start = (int) filter[0];
                int end = (int) filter[1];
                String day = (String) filter[2];
                if (!first) {
                    filterQuery += "OR ";
                }
                filterQuery += "(\"hora_inicio\" <= " + start + " AND \"hora_fin\" >= " + end + " AND \"dia\" = '" + day + "') ";
                first = false;
            }
        }
        //System.out.println(filterQuery);
        StringBuilder query = null;
        int size = User.getSelectedSubjects().size();
        if (!User.getSelectedSubjects().isEmpty()) {
            query = new StringBuilder("SELECT * FROM ");
            Subject last = User.getSelectedSubjects().getLast();
            String filter = "WHERE ";
            int k = 0;
            for (Subject selectedSubject : User.getSelectedSubjects()) {
                query.append("(SELECT \"nrc\" A" + k + " FROM \"Curso\" WHERE \"cod_asig\" = '")
                        .append(selectedSubject.getCode()).append("' " + filterQuery + (!filterQuery.equals("") ? ")" : "") + ")\n");
                if (!selectedSubject.equals(last)) {
                    query.append("CROSS JOIN\n");
                }
                for (int j = 0; j < selectedSubject.getCourses().values().size(); j++) {
                    Course c = (Course) selectedSubject.getCourses().values().toArray()[j];
                    if (!c.isEnable()) {
                        filter = filter + "A" + k + " != " + c.getNrc() + " AND ";
                    }
                }
                k++;
            }
            if (!filter.equals("WHERE ")) {
                filter = filter.substring(0, filter.length() - 5);
                query.append(filter + "\n");
            }
            query.append("OFFSET ").append(index).append(" ROWS FETCH NEXT 1 ROWS ONLY");

        }
        if (query != null) {
            return String.valueOf(query);
        }
        return null;
    }

    public static void getPossibleSchedule(){
        User.setPossibleSchedules(new LinkedList<>());
        ResultSet rs;
        try {
            psSavedSchedule.setInt(1,User.getCodeUser());
            rs = psSavedSchedule.executeQuery();
            while (rs.next()){
                User.getPossibleSchedules().add(new PossibleSchedule(rs.getInt(1),rs.getInt(2),rs.getString(3)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public static int getInfoUserByUsername(String username){
        ResultSet rs;
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT \"cod_estu\" FROM \"Credencial\" WHERE \"usuario\" = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void sendShareNotification(int codigo){
        String query = "INSERT INTO \"Solicitud\" (\"cod_estu_solicita\",\"cod_estu_comparte\",\"fechahora\",\"consecutivo\") " +
                "VALUES ("+User.getCodeUser()+","+codigo+",LOCALTIMESTAMP,NULL)";
        try {
            conn.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}