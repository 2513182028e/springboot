package management.dao;


import management.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TeacherDao {

    public class_teacher Class_teacher_information(String ID);

    public void updateClass_stu(class_student s);
   public Teacher queryTeacherByID(String ID);

    public List<ViewClass> queryTeachByID(String TeacherID);   //1

    public void updateTeachByID(class_teacher t);

    public void deleteTeachByID(String ID,String ClassID);

    public void addTeachByID(class_teacher t);

    public List<Classes> queryClassByID(String ID);
    public Classes ClassInformationByID(String ID);
    public void deleteClass_TeacherByID(String ID);


    public List<class_student> QueryStu_classByTeacherID(@Param("TeacherID") String TeacherID);
}
