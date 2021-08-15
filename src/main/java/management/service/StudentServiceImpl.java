package management.service;

import management.dao.StudentDao;
import management.entity.Classes;
import management.entity.Student;
import management.entity.class_student;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentServiceImpl  implements  StudentService{

   @Autowired
   private StudentDao studentDao;

   @Autowired
   @Qualifier("re")
   private RedisTemplate<String,Object> redisTemplate;

    @Override

    public Student queryByName(String username) {
       return studentDao.queryByName(username);
    }


    @Override

    public void JoinClass(String StudentID, String ClassID, String TeacherID) {
        studentDao.JoinClass(StudentID,ClassID,TeacherID);
    }

    @Override

    public void deleteClass(String StudentID, String ClassID, String TeacherID) {
        studentDao.deleteClass(StudentID,ClassID,TeacherID);
    }

    @Override
    public List<class_student> QueryChosenClasses(String TeacherID, String ClassID) {
        return studentDao.QueryChosenClasses(TeacherID,ClassID);
    }

    @Override

    public class_student selectClass_stu(String StudentID, String ClassID, String TeacherID) {
        return  studentDao.selectClass_stu(StudentID,ClassID,TeacherID);
    }


    @Override

    public void deleteChosen(@Param("ClassID") String ClassID,@Param("StudentID") String StudentID) {
        studentDao.deleteChosen(ClassID,StudentID);
    }

    @Override
    public String queryTeacherIDBye(String StudentID, String ClassID) {
        return studentDao.queryTeacherIDBye(StudentID,ClassID);
    }
}
