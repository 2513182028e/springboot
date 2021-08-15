package management.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import management.entity.*;
import management.service.ClassesServiceImpl;
import management.service.StudentServiceImpl;
import management.service.TeacherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
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
@RequestMapping("/Teacher")
public class TeacherController {

    @Autowired
    TeacherServiceImpl teacherService;

    @Autowired
    ClassesServiceImpl classesService;

    @Autowired
    StudentServiceImpl studentService;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

     @RequestMapping("/login")
    public String Login()
    {
        return "TeacherLogin";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession)
    {
        httpSession.removeAttribute("TeacherID");
        return "index";
    }
    @RequestMapping("/main")
    public String m(HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("TeacherID")!=null) {
            return "TeacherMain";
        }
        else
        {
            model.addAttribute("msg","请先登录");
            return "TeacherLogin";
        }
    }

    @RequestMapping("/login/Authentication")
    public String Authentication(
            HttpServletRequest httpServletRequest, Model model,HttpSession httpSession)
    {

        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
       Teacher teacher=teacherService.queryTeacherByID(username);
        if(teacher!=null)
        {
            String pass=teacher.getPassword();
            String salt=teacher.getSalt();
            String re=DigestUtils.md5DigestAsHex(password.getBytes());
            String result=DigestUtils.md5DigestAsHex((re+salt).getBytes());

            if(pass.equals(result))
            {
                httpSession.setAttribute("TeacherID",username);                        //MD5加密时，登录验证
                return "TeacherMain";
            }
        }
        model.addAttribute("msg","用户或密码错误，请再次登录");
            return "TeacherLogin";
    }
    @RequestMapping("/query")
    public String query(HttpSession httpSession,Model model)
    {
        String ID=(String) httpSession.getAttribute("TeacherID");
        //System.out.println("进入了查询功能");
        List<ViewClass> emps=teacherService.queryTeachByID(ID);   //查询增加所教的课
        if(emps!=null)
        {
            for (ViewClass viewClass : emps) {
                String TeacherID = viewClass.getTeacherID();
                String ClassID = viewClass.getClassID();
                Object k=redisTemplate.opsForValue().get("ClassNumber::"+TeacherID+ClassID);
                if(k!=null)
                {
                    int num=Integer.parseInt(k.toString());
                    viewClass.setClassNumber(num);                     //已经参加的课的人数进行设计
                }
                else
                {
                    int num = classesService.countNumber(TeacherID, ClassID);
                    viewClass.setClassNumber(num);
                    redisTemplate.opsForValue().set("ClassNumber::"+TeacherID+ClassID,num);
                }
                }
            }
            //System.out.println("查询到所教课程");
            /**for(int i=0;i<emps.size();i++)
            {
                System.out.println(emps);
            }**/
            model.addAttribute("empsList",emps);
            //model.addAttribute("empssList",empps);
            return "TeacherMainQuery";
    }
    @RequestMapping("/examine")
    @ResponseBody
    public void examine(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
          String ClassID=httpServletRequest.getParameter("ClassID");
          //System.out.println("我进来了AJAX");
          Classes classes=classesService.queryByID(ClassID);
          String json="此课程号不存在";
          if(classes!=null)
          {
              ObjectMapper om=new ObjectMapper();                           //Ajax，异步刷新来检查课程编号是否正确
               json="此课程号输入正确";
          }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out=httpServletResponse.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }

    @RequestMapping("/setPoints")
    public String ExamPoints(HttpSession httpSession,Model model)
    {
        List<class_student> class_students= new ArrayList<>();
        String TeacherID=(String) httpSession.getAttribute("TeacherID");                           //获取这个老师所教课程的学生选课情况，
        List<class_student> classStudents=teacherService.QueryStu_classByTeacherID(TeacherID);
        model.addAttribute("setClassesPoints",classStudents);
        return "TeacherMainScores";                                              
    }
    @RequestMapping("/StartSetPoints")
    public String  StartSetPoints(@RequestParam("StudentID") String StudentID,
                                  @RequestParam("ClassID") String ClassID,HttpSession httpSession,Model model)
    {
        String TeacherID=(String)httpSession.getAttribute("TeacherID");
        class_student q=studentService.selectClass_stu(StudentID,ClassID,TeacherID);      
        model.addAttribute("Class_Stu",q);                                       //获取要打分的具体学生的选课情况
       return "TeacherMainSetPoints";                                           //跳转至相关表单
    }

    @RequestMapping("/SetPointsOver")
    public String SetPointsOver(@RequestParam("StudentID") String StudentID,@RequestParam("ClassID") String ClassID,
                                @RequestParam("TeacherID") String TeacherID,
                                @RequestParam("ClassDurationPoints") String ClassDurationPoints,
                                @RequestParam("ClassFinalPoints") String ClassFinalPoints,
                                @RequestParam("ClassPoints") String ClassPoints,HttpSession httpSession,Model model)
    {
        class_student q=studentService.selectClass_stu(StudentID,ClassID,TeacherID);
        class_student c=new class_student();
        c.setClassID(ClassID);
        c.setStudentID(StudentID); 
        c.setTeacherIDD(TeacherID);
        int F=Integer.parseInt(ClassDurationPoints);  //课程平时成绩分
        int D=Integer.parseInt(ClassFinalPoints);       //课程期末成绩分
        if(F<0 || F>30)
        {                                                    //对于表单输入的分数进行检查，判断其合理性

            model.addAttribute("Class_Stu",q);
            model.addAttribute("msg","学生学号为:"+StudentID+"课程号为:"+ClassID+"的平时分错误"+"平时分应该在0-30之间");
            return "TeacherMainSetPoints";
        }
         if(D>70)
        {
            model.addAttribute("Class_Stu",q);
            model.addAttribute("msg","学生学号为:"+StudentID+"课程号为:"+ClassID+"的期末卷面分错误"+"期末分应该在0-70之间");
            return "TeacherMainSetPoints";
        }
        c.setClassDurationPoints(Integer.parseInt(ClassDurationPoints));
        c.setClassFinalPoints(Integer.parseInt(ClassFinalPoints));
        c.setClassPoints(D+F);
        teacherService.updateClass_stu(c);
        Boolean d=redisTemplate.hasKey("class_student::"+TeacherID);         //删除缓存，保证数据一致性
        if(d!=null)     
        {
            redisTemplate.delete("class_student::"+TeacherID);
        }
        return "redirect:/Teacher/setPoints";
    }

    @RequestMapping("/examinePoints")
    @ResponseBody
    public String examinePoints(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String ClassDurationPoints=httpServletRequest.getParameter("ClassDurationPoints");
        String ClassFinalPoints=httpServletRequest.getParameter("ClassFinalPoints");
        System.out.println("我进来了AJAX");
        int d=Integer.parseInt(ClassDurationPoints);
        int f=Integer.parseInt(ClassFinalPoints);
        String json="此分数在合理区间";
        if(d>=0&&d<=30&&f>=0&&f<=70)                          //Ajax，在填写表单期间检查分数的合理性
        {
            ObjectMapper om=new ObjectMapper();
            json="分数在合理区间";
        }
        else
        {
            json="平时分应在0-30间,期末分应在0-70分间";
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        return json;
    }

    @RequestMapping("/revocationPoints")
    public String revocationPoints(HttpSession httpSession,
                                   @RequestParam("StudentID") String StudentID,@RequestParam("ClassID") String ClassID)
    {
        String TeacherID=(String)httpSession.getAttribute("TeacherID");
             class_student d=studentService.selectClass_stu(StudentID,ClassID,TeacherID);
             d.setClassDurationPoints(0);
             d.setClassFinalPoints(0);
             d.setClassPoints(0);                          //对于已经打的分，清空归零
             teacherService.updateClass_stu(d);
        Boolean g=redisTemplate.hasKey("class_student::"+TeacherID);
        if(g!=null)
        {
            redisTemplate.delete("class_student::"+TeacherID);
        }
        return "redirect:/Teacher/setPoints";
    }
}
