package management.entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
@Repository
public class class_teacher implements Serializable {
    private String TeacherID;
    private String ClassID;
    //private int ClassNumber;
   // private String ClassStartTime;
   // private String ClassEndTime;
    //private int NumberMax;
    
    public class_teacher() {
    }

    public class_teacher(String teacherID, String classID) {
        TeacherID = teacherID;
        ClassID = classID;
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
}
