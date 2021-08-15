package management.dao;

import management.entity.Manager;
import management.entity.Student;
import management.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface  ManagerDao {
   //查询管理员信息
    public Manager queryManagerByID(String ManagerID);
    //学生的增删改查
    public void addStudent(Student student);
    public void    deleteStudent( String StudentID);
    public void   updateStudent(Student student);
    public Student  queryStudent(String StudentID);
    //查询全部学生
    public List<Student> queryStudentALL();


   //教师的增删改查
   public void addTeacher(Teacher teacher);
   public void deleteTeacher(String TeacherID);
    public void updateTeacher( Teacher teacher);
   public Teacher  queryTeacher(String TeacherID);
   //查询全部教师
    public List<Teacher> queryTeacherAll();

}
