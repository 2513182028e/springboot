package management.service;


import management.dao.TeacherDao;
import management.entity.*;
import management.utils.CastList;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl  implements  TeacherService{

    @Autowired
    TeacherDao teacherDao;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Cacheable(value = "Classes",key = "#ID")
    public List<Classes> queryClassByID(String ID) {

        return teacherDao.queryClassByID(ID);
    }

    @Override
    @Cacheable(value = "Classes",key = "#ID")
    public Classes ClassInformationByID(String ID) {
        return teacherDao.ClassInformationByID(ID);
    }

    @Override
    @Cacheable(value = "Teacher",key = "#ID")
    public Teacher queryTeacherByID(String ID) {
        return teacherDao.queryTeacherByID(ID);
    }

    @Override
    @Cacheable(value = "ViewClass",key = "#TeacherID")
    public List<ViewClass> queryTeachByID(String TeacherID) {                //1
        return teacherDao.queryTeachByID(TeacherID);
    }



    @Override
    @Cacheable(value = "class_teacher",key = "#ID")
    public class_teacher Class_teacher_information(String ID) {
        return teacherDao.Class_teacher_information(ID);
    }


    @Override
    public void updateClass_stu(class_student s) {
        teacherDao.updateClass_stu(s);
    }

    @Override
    @Cacheable(value = "class_student",key = "#TeacherID")
    public List<class_student> QueryStu_classByTeacherID(String TeacherID) {
        return teacherDao.QueryStu_classByTeacherID(TeacherID);
    }
}
