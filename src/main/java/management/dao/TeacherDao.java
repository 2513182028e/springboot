package management.dao;


import management.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TeacherDao {

    public class_teacher Class_teacher_information(String ID);     //通过教师编号查询该教师的任教信息

    public void updateClass_stu(class_student s);               //对学生进行打分
   public Teacher queryTeacherByID(String ID);                   //通过教师编号查询教师

    public List<ViewClass> queryTeachByID(String TeacherID);   //1

    public void updateTeachByID(class_teacher t);
                                    
    public void deleteTeachByID(String ID,String ClassID);     //任教关系的改变

    public void addTeachByID(class_teacher t);

    public List<Classes> queryClassByID(String ID);         //该功能已废除
    public Classes ClassInformationByID(String ID);         //通过课程编号来查询课程信息
    public void deleteClass_TeacherByID(String ID);         //从任教关系中删除与该课程编号有关的所用任教关系


    public List<class_student> QueryStu_classByTeacherID(@Param("TeacherID") String TeacherID);   //从class_student表中查询该教师编号有关的选课信息
}
