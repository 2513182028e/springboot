package management.service;

import management.entity.Classes;
import management.entity.Student;
import management.entity.class_student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    public Student queryByName(String username);


    public String queryTeacherIDBye(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID);
    public void deleteClass(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);

    public List<class_student> QueryChosenClasses(@Param("TeacherID") String TeacherID, @Param("ClassID") String ClassID);

    public void deleteChosen(@Param("ClassID") String ClassID,@Param("StudentID") String StudentID);

    public void JoinClass(@Param("StudentID") String StudentID, @Param("ClassID") String ClassID, @Param("TeacherID") String TeacherID);

    public class_student selectClass_stu(@Param("StudentID") String StudentID,@Param("ClassID") String ClassID,@Param("TeacherID") String TeacherID);
}
