package org.allyssinxd.myifoodjavaedition;

public class Register {
    private String date;
    private String start;
    private String end;
    private String averenge;
    private int acceptedRuns;
    private int deniedRuns;
    private float value;

    public Register(){}
    public Register(String date, float value, String start, String end, String averenge, int accepted, int denied){
        this.date = date;
        this.value = value;
        this.start = start;
        this.end = end;
        this.averenge = averenge;
        this.acceptedRuns = accepted;
        this.deniedRuns = denied;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAverenge() {
        return averenge;
    }

    public void setAverenge(String averenge) {
        this.averenge = averenge;
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
}
