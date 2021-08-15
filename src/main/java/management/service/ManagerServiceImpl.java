package management.service;


import management.dao.ManagerDao;
import management.entity.Manager;
import management.entity.Student;
import management.entity.Teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class ManagerServiceImpl implements  ManagerService{


    @Autowired
    private ManagerDao managerDao;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Cacheable(value = "Manager",key = "#ManagerID")
    public Manager queryManagerByID(String ManagerID) {
        return managerDao.queryManagerByID(ManagerID);
    }

    @Override
    @CachePut(value = "Student",key = "#result.StudentID")
    public Student addStudent(Student student) {
     managerDao.addStudent(student);
     return student;
    }

    @Override
    @CacheEvict(value = "Student",key = "#StudentID")
    public void deleteStudent(String StudentID) {
        managerDao.deleteStudent(StudentID);
    }

    @Override
    @CachePut(value = "Student",key ="#result.StudentID" )
    public Student updateStudent(Student student) {
      managerDao.updateStudent(student);
      return student;
    }

    @Override
    @Cacheable(value = "Student",key = "#StudentID")
    public Student queryStudent(String StudentID) {
        return managerDao.queryStudent(StudentID);
    }

    @Override
    @Cacheable(value = "Student",key = "0")
    public List<Student> queryStudentALL() {
        return managerDao.queryStudentALL();
    }

    @Override
    @CachePut(value = "Teacher",key = "#result.TeacherID")
    public Teacher addTeacher(Teacher teacher) {
   managerDao.addTeacher(teacher);
   return  teacher;
    }

    @Override
    @CacheEvict(value = "Teacher",key = "#TeacherID")
    public void deleteTeacher(String TeacherID) {
     managerDao.deleteTeacher(TeacherID);
    }

    @Override
    @CachePut(value = "Teacher",key ="#result.TeacherID" )
    public Teacher updateTeacher(Teacher teacher) {
       managerDao.updateTeacher(teacher);
       return teacher;
    }

    @Override
    @Cacheable(value = "Teacher",key = "#TeacherID")
    public Teacher queryTeacher(String TeacherID) {
        return managerDao.queryTeacher(TeacherID);
    }

    @Override
    @Cacheable(value = "Teacher",key = "0")
    public List<Teacher> queryTeacherAll() {
        return managerDao.queryTeacherAll();
    }
}
