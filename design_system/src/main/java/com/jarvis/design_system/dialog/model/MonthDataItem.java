package com.jarvis.design_system.dialog.model;

public class MonthDataItem {
    private int month;
    private int year;
    private State state;
    public MonthDataItem(int month, int year, State state) {
        this.month = month;
        this.year = year;
        this.state = state;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        NORMAL,
        SELECTED,
        DISABLE
    }
}
