package model;

public class Student {
    private static String username;
    private static int codeUser;
    private static String fullname;
    private static char gender;
    private static int idPlan;
    private static String period;

    private Student() {}

    public static char getGender() {
        return gender;
    }

    public static void setGender(char gender) {
        Student.gender = gender;
    }

    public static int getIdPlan() {
        return idPlan;
    }

    public static void setIdPlan(int idPlan) {
        Student.idPlan = idPlan;
    }

    public static String getPeriod() {
        return period;
    }

    public static void setPeriod(String period) {
        Student.period = period;
    }

    public static String getFullname() {
        return fullname;
    }

    public static void setFullname(String fullname) {
        Student.fullname = fullname;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Student.username = username;
    }

    public static int getCodeUser() {
        return codeUser;
    }

    public static void setCodeUser(int codeUser) {
        Student.codeUser = codeUser;
    }
}
