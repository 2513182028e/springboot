package management.dao;

import management.entity.Classes;
import management.entity.Student;
import management.entity.class_student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface StudentDao {

    public Student queryByName(String username);//学生编号查询学生信息

    public List<class_student> QueryChosenClasses(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID);     //从class——student表中查找选课关系

    public void deleteClass(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID); //退出已经选择的课
   

    public void JoinClass(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);////选课界面的（由ViewClass和thymeleaf）组成
     //ViewClass类保护教师ID，课程ID，学生选课
    public class_student selectClass_stu(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);

    public void deleteChosen(@Param("ClassID") String ClassID,@Param("StudentID") String StudentID); //退出已经选择的课


    public String queryTeacherIDBye(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID);//从class——student表中查询出教师


}
