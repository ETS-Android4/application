package com.gttime.android.component;

import com.gttime.android.util.StringUtil;
import com.github.tlaabs.timetableview.Time;

public class Course {
    private int courseTerm;
    private java.lang.String courseDay;
    private java.lang.String courseMajor;
    private java.lang.String courseTitle;
    private java.lang.String courseCRN;
    private java.lang.String courseArea;
    private java.lang.String courseSection;
    private java.lang.String courseClass;
    private java.lang.String courseTime;
    private Time startTime;
    private Time endTime;
    private java.lang.String courseLocation;
    private java.lang.String courseInstructor;
    private java.lang.String courseUniversity;
    private java.lang.String courseCredit;
    private java.lang.String courseAttribute;

    public Course(int courseTerm, java.lang.String courseDay, java.lang.String courseMajor, java.lang.String courseTitle, java.lang.String courseCRN, java.lang.String courseArea, java.lang.String courseSection, java.lang.String courseClass, java.lang.String courseTime, java.lang.String courseLocation, java.lang.String courseInstructor, java.lang.String courseUniversity, java.lang.String courseCredit, java.lang.String courseAttribute) {
        this.courseTerm = courseTerm;
        this.courseDay = courseDay;
        this.courseMajor = courseMajor;
        this.courseTitle = courseTitle;
        this.courseCRN = courseCRN;
        this.courseArea = courseArea;
        this.courseSection = courseSection;
        this.courseClass = courseClass;
        this.courseTime = courseTime;
        setStartTime(new CourseTime(StringUtil.split(courseTime,"-",2)[0]));
        setEndTime(new CourseTime(StringUtil.split(courseTime,"-",2)[1]));
        this.courseLocation = courseLocation;
        this.courseInstructor = courseInstructor;
        this.courseUniversity = courseUniversity;
        this.courseCredit = courseCredit;
        this.courseAttribute = courseAttribute;
    }

    public int getCourseTerm() {
        return courseTerm;
    }

    public void setCourseTerm(int courseTerm) {
        this.courseTerm = courseTerm;
    }

    public java.lang.String getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(java.lang.String courseDay) {
        this.courseDay = courseDay;
    }

    public java.lang.String getCourseMajor() {
        return courseMajor;
    }

    public void setCourseMajor(java.lang.String courseMajor) {
        this.courseMajor = courseMajor;
    }

    public java.lang.String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(java.lang.String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public java.lang.String getCourseCRN() {
        return courseCRN;
    }

    public void setCourseCRN(java.lang.String courseCRN) {
        this.courseCRN = courseCRN;
    }

    public java.lang.String getCourseArea() {
        return courseArea;
    }

    public void setCourseArea(java.lang.String courseArea) {
        this.courseArea = courseArea;
    }

    public java.lang.String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(java.lang.String courseSection) {
        this.courseSection = courseSection;
    }

    public java.lang.String getCourseClass() {
        return courseClass;
    }

    public void setCourseClass(java.lang.String courseClass) {
        this.courseClass = courseClass;
    }

    public java.lang.String getCourseLocation() {
        return courseLocation;
    }

    public void setCourseLocation(java.lang.String courseLocation) {
        this.courseLocation = courseLocation;
    }

    public java.lang.String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(java.lang.String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public java.lang.String getCourseUniversity() {
        return courseUniversity;
    }

    public void setCourseUniversity(java.lang.String courseUniversity) {
        this.courseUniversity = courseUniversity;
    }

    public java.lang.String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(java.lang.String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public java.lang.String getCourseAttribute() {
        return courseAttribute;
    }

    public void setCourseAttribute(java.lang.String courseAttribute) {
        this.courseAttribute = courseAttribute;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public java.lang.String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(java.lang.String courseTime) {
        this.courseTime = courseTime;
    }

}