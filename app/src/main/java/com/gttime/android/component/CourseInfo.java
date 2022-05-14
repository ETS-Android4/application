package com.gttime.android.component;

public class CourseInfo {
    Course course;
    Seat seat;

    public CourseInfo(Course course, Seat seat) {
        this.course = course;
        this.seat = seat;
    }

    public Course getCourse() {
        return course;
    }

    public Seat getSeat() {
        return seat;
    }
}
