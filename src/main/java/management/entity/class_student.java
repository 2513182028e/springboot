package management.entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Repository
public class class_student implements Serializable {
    private String StudentID;
    private String ClassID;
    private String TeacherIDD;
    private int ClassDurationPoints;
    private int ClassFinalPoints;
    private int ClassPoints;
    public class_student() {
    }

    @Override
    public String toString() {
        return "class_student{" +
                "StudentID='" + StudentID + '\'' +
                ", ClassID='" + ClassID + '\'' +
                ", TeacherIDD='" + TeacherIDD + '\'' +
                ", ClassDurationPoints=" + ClassDurationPoints +
                ", ClassFinalPoints=" + ClassFinalPoints +
                ", ClassPoints=" + ClassPoints +
                '}';
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getTeacherIDD() {
        return TeacherIDD;
    }

    public void setTeacherIDD(String teacherIDD) {
        TeacherIDD = teacherIDD;
    }

    public int getClassDurationPoints() {
        return ClassDurationPoints;
    }

    public void setClassDurationPoints(int classDurationPoints) {
        ClassDurationPoints = classDurationPoints;
    }

    public int getClassFinalPoints() {
        return ClassFinalPoints;
    }

    public void setClassFinalPoints(int classFinalPoints) {
        ClassFinalPoints = classFinalPoints;
    }

    public int getClassPoints() {
        return ClassPoints;
    }

    public void setClassPoints(int classPoints) {
        ClassPoints = classPoints;
    }

    public class_student(String studentID, String classID, String teacherIDD, int classDurationPoints, int classFinalPoints, int classPoints) {
        StudentID = studentID;
        ClassID = classID;
        TeacherIDD = teacherIDD;
        ClassDurationPoints = classDurationPoints;
        ClassFinalPoints = classFinalPoints;
        ClassPoints = classPoints;
    }
}
