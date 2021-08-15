package management.dao;


import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_student;
import management.entity.class_teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ClassesDao {


    public Classes queryByID(String ID);            //通过课程编号查询课程信息

    public List<class_teacher> QueryClassesAll();     //查询所有的教师任教关系
 
     public List<class_student> QueryClassesByID(String StudentID);   //通过学生编号查询该学生的选课信息

    public  List<ViewClass> QueryViewClassAll();                 //查询所以的课程结果（ViewClass类在Class类的基础时加入了TeacherID这个属性）

    public int countNumber(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID);   //从class——student表中查找选了某个老师教的某节课的学生人数，
    //用来设置ViewClass类中的ClassNumber的属性
}
