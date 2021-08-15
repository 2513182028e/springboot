package management.entity;


import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class Student  implements Serializable {


    private String StudentID;
    private String name;
    private String major;
    private int age;
    private String sex;
    private String college;
    private String salt;
    private String password;



    public Student(String studentID, String name, String major, int age, String sex, String college, String salt, String password) {
        StudentID = studentID;
        this.name = name;
        this.major = major;
        this.age = age;
        this.sex = sex;
        this.college = college;
        this.salt = salt;
        this.password = password;
    }

    public Student() {
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    @Override
    public String toString() {
        return "Student{" +
                "StudentID='" + StudentID + '\'' +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", college='" + college + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
