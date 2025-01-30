package org.allyssinxd.myifoodjavaedition;

import java.time.LocalDate;
import java.time.LocalTime;

public class Register {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String average;
    private int acceptedRuns;
    private int deniedRuns;
    private float value;

    public Register(){}
    public Register(LocalDate date, float value, LocalTime start, LocalTime end, String average, int accepted, int denied){
        this.date = date;
        this.value = value;
        this.start = start;
        this.end = end;
        this.average = average;
        this.acceptedRuns = accepted;
        this.deniedRuns = denied;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public int getAcceptedRuns() {
        return acceptedRuns;
    }

    public void setAcceptedRuns(int acceptedRuns) {
        this.acceptedRuns = acceptedRuns;
    }

    public int getDeniedRuns() {
        return deniedRuns;
    }

    public void setDeniedRuns(int deniedRuns) {
        this.deniedRuns = deniedRuns;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
