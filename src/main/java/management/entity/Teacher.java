package management.entity;

import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class Teacher implements Serializable {
    private String TeacherID;
    private String TeacherName;
    private String Password;
    private String major;
    private String college;
   private String salt;



    public Teacher() {
    }
    @Override
    public String toString() {
        return "Teacher{" +
                "TeacherID='" + TeacherID + '\'' +
                ", TeacherName='" + TeacherName + '\'' +
                ", Password='" + Password + '\'' +
                ", major='" + major + '\'' +
                ", college='" + college + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String teacherID) {
        TeacherID = teacherID;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Teacher(String teacherID, String teacherName, String password, String major, String college, String salt) {
        TeacherID = teacherID;
        TeacherName = teacherName;
        Password = password;
        this.major = major;
        this.college = college;
        this.salt = salt;
    }
}
