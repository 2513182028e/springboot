

package management.controller;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import management.entity.Classes;
import management.entity.Teacher;
import management.entity.ViewClass;
import management.entity.class_teacher;
import management.service.ClassesServiceImpl;
import management.service.ManagerClassesServiceImpl;
import management.service.TeacherServiceImpl;
import management.utils.CastList;
import management.utils.ClassesTime;
import management.utils.ViesComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/Manager/Classes")
public class ManagerClassesController {

    @Autowired
    private ManagerClassesServiceImpl managerClassesService;

    @Autowired
    private ClassesServiceImpl classesService;

    @Autowired
    private TeacherServiceImpl teacherService;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/query")
    public String QueryClasses(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "index";
        }
       List<ViewClass>  v=managerClassesService.queryViewClassAll();         //得到全部的课程（ViewClass）信息
        for (ViewClass viewClass : v) {
            String TeacherID = viewClass.getTeacherID();
            String ClassID = viewClass.getClassID();
               Object k=redisTemplate.opsForValue().get("ClassNumber::"+TeacherID+ClassID);       //从Redis缓存中查找该课程已参加人数
               if(k!=null)
               {
                   int num=Integer.parseInt(k.toString());
                   viewClass.setClassNumber(num);
               }
               else
               {
                   int num = classesService.countNumber(TeacherID, ClassID);
                   viewClass.setClassNumber(num);
                   redisTemplate.opsForValue().set("ClassNumber::"+TeacherID+ClassID,num);
               }
        }
        model.addAttribute("ManagerClasses",v);
        return "ManagerMainClasses";
    }
    @RequestMapping("/ClassInformation")
    public String ClassInformation(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "index";
        }
        List<Classes> classes=managerClassesService.queryClassesAll();    //获得课程信息（不含TeacherID）
        model.addAttribute("ClassesInformation",classes);
        return "ManagerMainClassesInformation";
    }
    @RequestMapping("/UpdateClasses")
    public String UpdateClasses(@RequestParam("ClassID") String ClassID,
                                Model model, HttpSession httpSession)
    {

        Classes classes=classesService.queryByID(ClassID);
        model.addAttribute("ManagerClass",classes);
        return "ManagerMainClassUpdate";
    }
    @RequestMapping("/UpdateOver")
    public String UpdateClassOver(
            @RequestParam("ClassID") String ClassID,
            @RequestParam("ClassName") String ClassName,
            @RequestParam("Context") String Context,
            @RequestParam("ClassDurationPoints") String ClassDurationPoints,
            @RequestParam("ClassFinalPoints") String ClassFinalPoints,
            @RequestParam("ClassPoints") String ClassPoints,
            @RequestParam("NumberMax") String NumberMax,
            @RequestParam("Place") String Place,
            @RequestParam("Time") String Time, Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        Classes classess=classesService.queryByID(ClassID);
        if(ClassID.equals("")||ClassName.equals("")||Context.equals("")||ClassDurationPoints.equals("")||
                ClassFinalPoints.equals("")||ClassPoints.equals("")||NumberMax.equals("")||               //判断表单的完整性
                Place.equals("")||Time.equals(""))
        {
            model.addAttribute("msg","请填写完整的课程信息");
            model.addAttribute("ManagerClass",classess);
            return "ManagerMainClassUpdate";
        }
        List<Classes> classes1=managerClassesService.queryClassesAll();           
        for(int i=0;i<classes1.size();i++)
        {
            Classes c=classes1.get(i);
            if(c.getClassID().equals(ClassID))
            {
                continue;
            }
            if(ClassesTime.Com(c.getTime(),Time)&&Place.equals(c.getPlace()))               //ClassesTime.Com（）用来判断两个课的时间是否冲突，具体实现见工具类
            {
                model.addAttribute("msg","修改课程与已存在的"+c.getClassID()+"号课程存在时间冲突");        
                model.addAttribute("ClassesInformation",classes1);
                return "ManagerMainClassesInformation";
            }
        }
        Classes classes=new Classes();
        classes.setClassID(ClassID);
        classes.setClassName(ClassName);
        classes.setContext(Context);
        classes.setClassDurationPoints(ClassDurationPoints);
        classes.setClassFinalPoints(ClassFinalPoints);
        classes.setClassPoints(ClassPoints);
        classes.setPlace(Place);
        classes.setTime(Time);
        classes.setNumberMax(Integer.parseInt(NumberMax));
        Classes d=managerClassesService.updateClass(classes);
        Boolean f=redisTemplate.hasKey("ViewClass::0");                      //更新Class课程时，因此ViewClass类是Class表和Class——teacher表连接而成，故当Class课程改变时，
        //缓存ViewClass：：0 (代表所以选课信息) 需要失效，避免Redis和Mysql的数据不一致
        if(f!=null)
        {
            redisTemplate.delete("ViewClass::0");
            Set<String> keys=redisTemplate.keys("ViewClass::"+"*");
            if(keys!=null)
            {
                redisTemplate.delete(keys);
            }
        }
        Boolean g=redisTemplate.hasKey("Classes::0");
        if(g!=null)
        {
            redisTemplate.delete(
                    "Classes::0");              //同理（Classes：：0）含有所以课程信息，该缓存也需要失效
        }

        return "redirect:/Manager/Classes/ClassInformation";
    }

    @RequestMapping("/AddClasses")
    public String AddClasses(HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        return "ManagerMainClassAdd";       //增加课程时填写课程信息的表单，该表单填写完成后，发送请求到下面的"/AddOver"
    }


    @RequestMapping("/AddUOver")
    public String AddClassesOver(
            @RequestParam("ClassID") String ClassID,
            @RequestParam("ClassName") String ClassName,
            @RequestParam("Context") String Context,
            @RequestParam("ClassDurationPoints") String ClassDurationPoints,
            @RequestParam("ClassFinalPoints") String ClassFinalPoints,
            @RequestParam("ClassPoints") String ClassPoints,
            @RequestParam("NumberMax") String NumberMax,
            @RequestParam("Place") String Place,
            @RequestParam("Time") String Time, Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        if(ClassID.equals("")||ClassName.equals("")||Context.equals("")||ClassDurationPoints.equals("")||
                ClassFinalPoints.equals("")||ClassPoints.equals("")||NumberMax.equals("")||
                Place.equals("")||Time.equals(""))
        {
            model.addAttribute("msg","请填写完整的课程信息");
            return "ManagerMainClassAdd";
        }
        List<Classes> classes1=managerClassesService.queryClassesAll();
        for(int i=0;i<classes1.size();i++)
        {
            Classes c=classes1.get(i);
            if(ClassesTime.Com(c.getTime(),Time)&&Place.equals(c.getPlace()))
            {
                 model.addAttribute("msg","新增课程与已存在的"+c.getClassID()+"号课程存在时间冲突");   //判断新增课程与已有课程是否时间冲突
                 model.addAttribute("ClassesInformation",classes1);
                 return "ManagerMainClassesInformation";
            }
        }
        Classes classes=new Classes();
        classes.setClassID(ClassID);
        classes.setClassName(ClassName);
        classes.setContext(Context);
        classes.setClassDurationPoints(ClassDurationPoints);
        classes.setClassFinalPoints(ClassFinalPoints);
        classes.setClassPoints(ClassPoints);
        int Numberd=NumberMax.equals(" ")?0:Integer.parseInt(NumberMax);
        classes.setNumberMax(Numberd);
        classes.setPlace(Place);
        classes.setTime(Time);
       Classes d= managerClassesService.AddClasses(classes);     //添加新课程
        Boolean f=redisTemplate.hasKey("ViewClass::0");
        if(f!=null)
        {
            redisTemplate.delete("ViewClass::0");
            Set<String> keys=redisTemplate.keys("ViesClass::"+"*");              //缓存要主动删除，原因上面一样
            if(keys!=null)
            {
                redisTemplate.delete(keys);
            }
        }
        Boolean g=redisTemplate.hasKey("Classes::0");
        if(g!=null)
        {
            redisTemplate.delete("Classes::0");
        }
        Set<String> keys=redisTemplate.keys("ClassNumber::"+"*");
        if(keys!=null)
        {
            redisTemplate.delete(keys);
        }
        return "redirect:/Manager/Classes/ClassInformation";
    }

    @RequestMapping("/deleteClasses")
    public String deleteClasses(@RequestParam("ClassID") String ClassID,
                                Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        managerClassesService.deleteClasses(ClassID);
        Boolean k=redisTemplate.hasKey("ViewClass::0");
        if(k!=null)
        {
            redisTemplate.delete("ViewClass::0");
            Set<String> keys=redisTemplate.keys("ViewClass::"+"*");
            if(keys!=null)
            {
                redisTemplate.delete(keys);
            }
        }
        Boolean g=redisTemplate.hasKey("Classes::0");
        if(g!=null)
        {
            redisTemplate.delete("Classes::0");
        }
        Set<String> keys=redisTemplate.keys("ClassNumber::"+"*");
        if(keys!=null)
        {
            redisTemplate.delete(keys);
        }
        return "redirect:/Manager/Classes/ClassInformation";
    }

    @RequestMapping("/deleteRelation")
    public String delete(@RequestParam("TeacherID") String TeacherID,@RequestParam("ClassID") String ClassID,           //修改任教关系（class——teacher表）
                         Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
         managerClassesService.deleteRelation(TeacherID,ClassID);
        Boolean f=redisTemplate.hasKey("ViewClass::0");            //ViewClass 由Class表和Class——teacher表连接而成，故任教关系改变，该缓存也要失效
        if(f!=null)
        {
            redisTemplate.delete("ViewClass::0");
        }
        Object  k=redisTemplate.opsForValue().get("ViewClass::"+TeacherID);    //被修改老师的任教缓存删除
        if(k!=null)
        {
            redisTemplate.delete("ViewClass::"+TeacherID);
        }
        Object l=redisTemplate.opsForValue().get("class_student::"+TeacherID);
        if(l!=null)
        {
            redisTemplate.delete("class_student::"+TeacherID);            //由于后续学生的选课情况中，涉及TeacherID,ClassID,StudentID,故此缓存也要删除
        }
        Set<String> keys=redisTemplate.keys("ClassNumber::"+"*");
        if(keys!=null)
        {
            redisTemplate.delete(keys);
        }
         return "redirect:/Manager/Classes/query";
    }

    @RequestMapping("/addRelation")
    public String AddRelation(HttpSession httpSession,Model model)


    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        return "ManagerRelationAdd";
    }


    @RequestMapping("/addRelationOver")
   public String Add(@RequestParam("TeacherID") String TeacherID,@RequestParam("ClassID")
            String ClassID,Model model,HttpSession httpSession)
   {
       if(httpSession.getAttribute("ManagerID")==null)
       {
           model.addAttribute("msg","请先登录");
           return "ManagerLogin";
       }
       Teacher teachert=teacherService.queryTeacherByID(TeacherID);
       if(teachert==null)
       {
           model.addAttribute("msg","教师编号错误,该老师不存在");
           return "ManagerRelationAdd";
       }
       if(classesService.queryByID(ClassID)==null)
       {
           model.addAttribute("msg","课程号错误，该课程不存在");
           return "ManagerRelationAdd";
       }
       class_teacher g=managerClassesService.queryClass_teacher(TeacherID,ClassID);
       if(g!=null)
       {
           model.addAttribute("msg","该任教关系已经存在，请重新选择");
           return "ManagerRelationAdd";
       }
       class_teacher d=new class_teacher();
       d.setTeacherID(TeacherID);
       d.setClassID(ClassID);
       class_teacher l=managerClassesService.addRelation(d);
       Object j=redisTemplate.opsForValue().get("ViewClass::0");
       if(j!=null)
       {
           redisTemplate.delete("ViewClass::0");
       }
       Object  k=redisTemplate.opsForValue().get("ViewClass::"+TeacherID);              //新增任教关系，删除缓存原因与上面一致
       if(k!=null)
       {
           redisTemplate.delete("ViewClass::"+TeacherID);
       }
       Object h=redisTemplate.opsForValue().get("class_student::"+TeacherID);
       if(h!=null)
       {
           redisTemplate.delete("class_student::"+TeacherID);
       }
       Set<String> keys=redisTemplate.keys("ClassNumber::"+"*");
       if(keys!=null)
       {
           redisTemplate.delete(keys);
       }

       return "redirect:/Manager/Classes/query";
   }
   @RequestMapping("/updateRelation")
   public String updateRelation(@RequestParam("TeacherID") String TeacherID,@RequestParam("ClassID") String ClassID
   ,Model model,HttpSession httpSession)
   {
       if(httpSession.getAttribute("ManagerID")==null)
       {
           model.addAttribute("msg","请先登录");
           return "ManagerLogin";
       }
       class_teacher class_teacher=managerClassesService.queryClass_teacher(TeacherID,ClassID);
       model.addAttribute("Relation",class_teacher);

       return "ManagerRelationUpdate";
   }

   @RequestMapping("/updateRelationOver")
   public String updateRelationOver(@RequestParam("TeacherID") String TeacherID,
                                    @RequestParam("ClassID") String ClassID,@RequestParam("NEWClassID")
                                               String newClassID
           , Model model,HttpSession httpSession)
   {
       if(httpSession.getAttribute("ManagerID")==null)
       {
           model.addAttribute("msg","请先登录");
           return "ManagerLogin";
       }
       class_teacher d=managerClassesService.queryClass_teacher(TeacherID,ClassID);
       Classes classes=classesService.queryByID(newClassID);
       if(classes==null)
       {
           System.out.println("课程找不到");
           model.addAttribute("msg","课程号错误，该课程不存在");
           model.addAttribute("Relation",d);
           return "ManagerRelationUpdate";
       }
       class_teacher g=managerClassesService.queryClass_teacher(TeacherID,newClassID);
       if(g!=null)
       {

           model.addAttribute("msg","该任教关系已经存在，请重新选择");
           model.addAttribute("Relation",d);
           return "ManagerRelationUpdate";
       }
       class_teacher jl=managerClassesService.updateRelation(TeacherID,ClassID,newClassID);
       Boolean f=redisTemplate.hasKey("ViewClass::0");
       if(f!=null)
       {
           redisTemplate.delete("ViewClass::0");
       }
       Object  k=redisTemplate.opsForValue().get("ViewClass::"+TeacherID);
       if(k!=null)
       {
           redisTemplate.delete("ViewClass::"+TeacherID);
       }
       Object l=redisTemplate.opsForValue().get("class_student::"+TeacherID);
       if(l!=null)
       {
           redisTemplate.delete("class_student::"+TeacherID);
       }
       Set<String> keys=redisTemplate.keys("ClassNumber::"+"*");
       if(keys!=null)
       {
           redisTemplate.delete(keys);
       }
       return "redirect:/Manager/Classes/query";
   }

   @RequestMapping("/InferClasses")
   @ResponseBody
   public String InforClass(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

       String ClassID=httpServletRequest.getParameter("ClassID");
       Classes classes=classesService.queryByID(ClassID);                    //Ajax，异步刷新，当输入课程编号时，表单下面的课程名称自动填充 ：课程不存在或课程名
       String jsono="该课程不存在";
       if(classes!=null)
       {
           ObjectMapper om=new ObjectMapper();
           jsono=classes.getClassName();
       }
       httpServletResponse.setContentType("application/json;charset=utf-8");
       return jsono;
   }
}
