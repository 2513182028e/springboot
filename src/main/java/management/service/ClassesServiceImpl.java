package management.service;


import com.alibaba.fastjson.JSON;
import management.dao.ClassesDao;
import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_student;
import management.entity.class_teacher;
import management.utils.CastList;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassesServiceImpl implements ClassesService {

    @Autowired
    private ClassesDao classesDao;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Cacheable(value = "Classes",key = "#ID")
    public Classes queryByID(String ID) {
        return classesDao.queryByID(ID);
    }


    @Override
    @Cacheable(value = "class_teacher",key = "0")
    public List<class_teacher> QueryClassesAll() {
       return classesDao.QueryClassesAll();
    }

    @Override
    //@CachePut(value = "class_student",key = "#StudentID")
    public List<class_student> QueryClassesByID(String StudentID) {
       return classesDao.QueryClassesByID(StudentID);
    }


    @Override
    @Cacheable(value = "ViewClass",key = "0")
    public List<ViewClass> QueryViewClassAll() {
        return classesDao.QueryViewClassAll();
    }


    @Override
    public int countNumber(@Param("TeacherID") String TeacherID, @Param("ClassID") String ClassID)
    {
        return classesDao.countNumber(TeacherID,ClassID);
    }
}
