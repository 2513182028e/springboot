package management.entity;


import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class ViewClass implements Serializable {
    private String TeacherID;
    private String ClassID;
    private String ClassName;
    private String Context;
    private String ClassDurationPoints;
    private String ClassFinalPoints;
    private String ClassPoints;
    private int ClassNumber;
    private int NumberMax;
    private String Place;
    private String Time;

    public ViewClass() {
    }
    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String teacherID) {
        TeacherID = teacherID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getClassDurationPoints() {
        return ClassDurationPoints;
    }

    public void setClassDurationPoints(String classDurationPoints) {
        ClassDurationPoints = classDurationPoints;
    }

    public String getClassFinalPoints() {
        return ClassFinalPoints;
    }

    public void setClassFinalPoints(String classFinalPoints) {
        ClassFinalPoints = classFinalPoints;
    }

    public String getClassPoints() {
        return ClassPoints;
    }

    public void setClassPoints(String classPoints) {
        ClassPoints = classPoints;
    }

    public int getClassNumber() {
        return ClassNumber;
    }

    public void setClassNumber(int classNumber) {
        ClassNumber = classNumber;
    }

    public int getNumberMax() {
        return NumberMax;
    }

    public void setNumberMax(int numberMax) {
        NumberMax = numberMax;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public ViewClass(String teacherID, String classID, String className, String context, String classDurationPoints, String classFinalPoints, String classPoints, int classNumber, int numberMax, String place, String time) {
        TeacherID = teacherID;
        ClassID = classID;
        ClassName = className;
        Context = context;
        ClassDurationPoints = classDurationPoints;
        ClassFinalPoints = classFinalPoints;
        ClassPoints = classPoints;
        ClassNumber = classNumber;
        NumberMax = numberMax;
        Place = place;
        Time = time;
    }
}




