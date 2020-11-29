package model;

import database.DatabaseManager;

import java.util.HashMap;
import java.util.LinkedList;

public class User {
    private static String username;
    private static int codeUser;
    private static String fullname;
    private static char gender;
    private static int idPlan;
    private static String period;
    private static HashMap<String, Subject> projection;
    private static LinkedList<Subject> selectedSubjects = new LinkedList<>();
    private static LinkedList<Course> currentCourses = new LinkedList<>();
    private static int activeIndexSchedule = 0;
    private static int cantGeneratedSchedules;
    private static LinkedList<Object[]> filters = new LinkedList<>();
    private static LinkedList<PossibleSchedule> pSchedules = new LinkedList<>();
    private static LinkedList<Request> requests = new LinkedList<>();
    private User() {
    }

    public static char getGender() {
        return gender;
    }

    public static void setGender(char gender) {
        User.gender = gender;
    }

    public static int getIdPlan() {
        return idPlan;
    }

    public static void setIdPlan(int idPlan) {
        User.idPlan = idPlan;
    }

    public static String getPeriod() {
        return period;
    }

    public static void setPeriod(String period) {
        User.period = period;
    }

    public static String getFullname() {
        return fullname;
    }

    public static void setFullname(String fullname) {
        User.fullname = fullname;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static int getCodeUser() {
        return codeUser;
    }

    public static void setCodeUser(int codeUser) {
        User.codeUser = codeUser;
    }

    public static HashMap<String, Subject> getProjection() {
        return projection;
    }

    public static void setProjection(HashMap<String, Subject> projection) {
        User.projection = projection;
    }

    public static LinkedList<Subject> getSelectedSubjects() {
        return selectedSubjects;
    }

    public static void setSelectedSubjects(LinkedList<Subject> selectedSubjects) {
        User.selectedSubjects = selectedSubjects;
    }

    public static void addSelectedSubject(Subject subject) {
        selectedSubjects.add(subject);
    }

    public static int getActiveIndexSchedule() {
        return activeIndexSchedule;
    }

    public static void setActiveIndexSchedule(int activeIndexSchedule) {
        User.activeIndexSchedule = activeIndexSchedule;
    }

    public static int getCantGeneratedSchedules() {
        return cantGeneratedSchedules;
    }

    public static void setCantGeneratedSchedules(int cantGeneratedSchedules) {
        User.cantGeneratedSchedules = cantGeneratedSchedules;
    }

    public static LinkedList<Course> getCurrentCourses() {
        return currentCourses;
    }

    public static void setCurrentCourses(LinkedList<Course> currentCourses) {
        User.currentCourses = currentCourses;
    }

    public static LinkedList<Object[]> getFilters() {
        return filters;
    }

    public static void setFilters(LinkedList<Object[]> filters) {
        User.filters = filters;
    }

    public static LinkedList<PossibleSchedule> getPossibleSchedules() {

        return User.pSchedules;
    }

    public static LinkedList<PossibleSchedule> getpSchedules() {
        return pSchedules;
    }

    public static LinkedList<Request> getRequests() {
        return requests;
    }

    public static void setPossibleSchedules(LinkedList<PossibleSchedule> pSchedules) {
        User.pSchedules = pSchedules;
    }
}
