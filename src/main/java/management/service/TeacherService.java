package management.service;


import management.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {

    public Teacher queryTeacherByID(String ID);


    public List<ViewClass> queryTeachByID(String TeacherID); //1



    public void updateClass_stu(class_student s);

    public List<Classes> queryClassByID(String ID);

    public Classes ClassInformationByID(String ID);

    public class_teacher Class_teacher_information(String ID);



    public List<class_student> QueryStu_classByTeacherID(@Param("TeacherID") String TeacherID);
}
