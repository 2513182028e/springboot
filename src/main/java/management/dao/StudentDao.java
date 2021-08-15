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

    public Student queryByName(String username);//1

    public List<class_student> QueryChosenClasses(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID);

    public void deleteClass(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);

    public void JoinClass(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);//1

    public class_student selectClass_stu(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);

    public void deleteChosen(@Param("ClassID") String ClassID,@Param("StudentID") String StudentID); //1


    public String queryTeacherIDBye(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID);


}
