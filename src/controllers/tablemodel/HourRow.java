package controllers.tablemodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HourRow {

    StringProperty hour;
    StringProperty monday;
    StringProperty tuesday;
    StringProperty wednesday;
    StringProperty thursday;
    StringProperty friday;
    StringProperty saturday;

    public HourRow(String hour, String monday, String tuesday, String wednesday,
                   String thursday, String friday, String saturday) {
        this.hour = new SimpleStringProperty(hour);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
    }

    public String getHour() {
        return hour.get();
    }

    public StringProperty hourProperty() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour.set(hour);
    }

    public String getMonday() {
        return monday.get();
    }

    public StringProperty mondayProperty() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday.set(monday);
    }

    public String getTuesday() {
        return tuesday.get();
    }

    public StringProperty tuesdayProperty() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday.set(tuesday);
    }

    public String getWednesday() {
        return wednesday.get();
    }

    public StringProperty wednesdayProperty() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday.set(wednesday);
    }

    public String getThursday() {
        return thursday.get();
    }

    public StringProperty thursdayProperty() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday.set(thursday);
    }

    public String getFriday() {
        return friday.get();
    }

    public StringProperty fridayProperty() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday.set(friday);
    }

    public String getSaturday() {
        return saturday.get();
    }

    public StringProperty saturdayProperty() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday.set(saturday);
    }

    public void setFromIndex(int i, String value) {

        switch (i) {
            case 1:
                setMonday(value);
                break;
            case 2:
                setTuesday(value);
                break;
            case 3:
                setWednesday(value);
                break;
            case 4:
                setThursday(value);
                break;
            case 5:
                setFriday(value);
                break;
            case 6:
                setSaturday(value);
                break;
        }
    }

    public String getFromIndex(int i) {
        String val="";
        switch (i) {
            case 1:
                val=getMonday();
                break;
            case 2:
                val=getTuesday();
                break;
            case 3:
                val=getWednesday();
                break;
            case 4:
                val=getThursday();
                break;
            case 5:
                val=getFriday();
                break;
            case 6:
                val=getSaturday();
                break;
        }
        return val;
    }
}