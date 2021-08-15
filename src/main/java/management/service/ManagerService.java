package management.service;


import management.entity.Manager;
import management.entity.Student;
import management.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ManagerService {


    public Manager queryManagerByID(String ManagerID);
    //学生的增删改查
    public Student addStudent(Student student);
    public void    deleteStudent( String StudentID);
    public Student   updateStudent(Student student);
    public Student  queryStudent(String StudentID);
    //查询全部学生
    public List<Student> queryStudentALL();


    //教师的增删改查
    public Teacher addTeacher(Teacher teacher);
    public void deleteTeacher(String TeacherID);
    public Teacher updateTeacher( Teacher teacher);
    public Teacher  queryTeacher(String TeacherID);
    //查询全部教师
    public List<Teacher> queryTeacherAll();


}
