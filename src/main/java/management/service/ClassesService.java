package management.service;


import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_student;
import management.entity.class_teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassesService {
    public Classes queryByID(String ID);

    public List<class_teacher> QueryClassesAll();

    public List<class_student> QueryClassesByID(String StudentID);
    public  List<ViewClass> QueryViewClassAll();

    public int countNumber(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID);


}
