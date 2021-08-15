package management.service;


import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_teacher;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManagerClassesService {


    public class_teacher queryClass_teacher(String TeacherID,String ClassID);

    public List<Classes> queryClassesAll();

    public Classes AddClasses(Classes classes);

    public List<ViewClass> queryViewClassAll();

    public Classes updateClass(Classes classes);

    public void deleteClasses(@Param("ClassID") String ClassID);
    public void deleteRelation(String TeacherID,String ClassID);
    public class_teacher addRelation(class_teacher C);

    public class_teacher updateRelation(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID,
                               @Param("newClassID") String newClassID);


}
