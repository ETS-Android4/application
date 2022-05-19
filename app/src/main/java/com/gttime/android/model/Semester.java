package com.gttime.android.model;

public class Semester {
    private String semesterText;
    private String semesterID;

    public Semester(String semesterText, String semesterID) {
        this.semesterText = semesterText;
        this.semesterID = semesterID;
    }

    public String getSemesterText() {
        return semesterText;
    }

    public void setSemesterText(String semesterText) {
        this.semesterText = semesterText;
    }

    public String getSemesterID() {
        return semesterID;
    }

    public void setSemesterID(String semesterID) {
        this.semesterID = semesterID;
    }
}
