package com.jarvis.design_system.dialog.model;

public class YearDataItem {
    private int year;
    private State state;
    public YearDataItem(int year, State state) {
        this.year = year;
        this.state = state;
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
        SELECTED
    }
}
