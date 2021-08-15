package management.service;


import com.sun.org.apache.xpath.internal.operations.Bool;
import management.dao.ManagerClassDao;
import management.entity.Classes;
import management.entity.ViewClass;
import management.entity.class_teacher;
import org.apache.ibatis.annotations.Param;
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
public class ManagerClassesServiceImpl  implements  ManagerClassesService{

    @Autowired
    private ManagerClassDao managerClassDao;

    @Autowired
    private ClassesServiceImpl classesService;
    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Cacheable(value = "Classes",key = "0")
    public List<Classes> queryClassesAll() {
        return managerClassDao.queryClassesAll();
    }

    @Override
    @CachePut(value = "Classes",key = "#result.ClassID")
    public Classes AddClasses(Classes classes) {
         managerClassDao.AddClasses(classes);
         return classes;
    }

    @Override
    @Cacheable(value = "ViewClass",key = "0")
    public List<ViewClass> queryViewClassAll() {
        return managerClassDao.queryViewClassAll();
    }

    @Override
    @CachePut(value = "Classes",key = "#result.ClassID")
    public Classes updateClass(Classes classes) {
      managerClassDao.updateClass(classes);
      return classes;
    }


    @Override
    @CacheEvict(value = "Classes",key = "#ClassID")
    public void deleteClasses(String ClassID) {
        managerClassDao.deleteClasses(ClassID);
    }

    @Override
    @CachePut(value = "class_teacher",key = "#result.TeacherID+#result.ClassID")
    public class_teacher addRelation(class_teacher C) {
        managerClassDao.addRelation(C);
        return C;
    }

    @Override
    @CacheEvict(value = "class_teacher",key = "#TeacherID+#ClassID")
    public void deleteRelation(String TeacherID, String ClassID) {
        managerClassDao.deleteRelation(TeacherID,ClassID);
    }

    @Override
   @CachePut(value = "class_teacher",key = "#result.TeacherID+#result.ClassID")
    public class_teacher updateRelation(String TeacherID, String ClassID, String newClassID) {
        managerClassDao.updateRelation(TeacherID,ClassID,newClassID);
        if(redisTemplate.opsForValue().get(TeacherID+ClassID)!=null)
        {
            redisTemplate.delete(TeacherID+ClassID);
        }
        class_teacher c=new class_teacher();
        c.setTeacherID(TeacherID);
        c.setClassID(newClassID);
        return c;
    }

    @Override
    @Cacheable(value = "class_teacher",key = "#TeacherID+#ClassID")
    public class_teacher queryClass_teacher(String TeacherID, String ClassID) {
        return managerClassDao.queryClass_teacher(TeacherID,ClassID);
    }
}
