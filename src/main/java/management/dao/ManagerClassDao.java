package management.dao;


import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ManagerClassDao {


   public class_teacher queryClass_teacher(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID); //从class——teacher表中查询任教关系

    public List<Classes> queryClassesAll();  //查找所以课程信息

    public void AddClasses(Classes classes);   

    public List<ViewClass> queryViewClassAll();   //ViewClass类在Classes类中加入了TeacherID属性

    public void updateClass(Classes classes);

    public void deleteClasses(@Param("ClassID") String ClassID); //1

    public void addRelation(class_teacher C);
    public void deleteRelation(@Param("TeacherID") String TeacherID,
                               @Param("ClassID") String ClassID);



    public void updateRelation(@Param("TeacherID") String TeacherID,@Param("ClassID") String ClassID,
                               @Param("newClassID") String newClassID);          //更新任教关系，ClassID原教课编号，newClassID，修改后所教课程的编号




}
