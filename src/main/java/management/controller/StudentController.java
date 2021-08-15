package management.controller;



import management.entity.*;
import management.service.ClassesServiceImpl;
import management.service.StudentServiceImpl;
import management.service.TeacherServiceImpl;
import management.utils.CastList;
import management.utils.ClassesTime;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.omg.CORBA.TIMEOUT;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/Student")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private ClassesServiceImpl classesService;
    @Autowired
    private TeacherServiceImpl teacherService;

    @Autowired
    @Qualifier("ra")
    private RedissonClient redissonClient;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/login")
    public String login() {
        return "StudentLogin";
    }
    @RequestMapping("/LoginOut")
    public String logout(HttpSession httpSession)
    {
        httpSession.removeAttribute("username");
        return "index";
    }

    @RequestMapping("/login/Authentication")
    public String Authentication(HttpServletRequest httpServletRequest, Model model,HttpSession httpSession) {

        if(httpSession.getAttribute("username")!=null)
        {
            return "StudentMain";
        }
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        Student student=studentService.queryByName(username);
        if(student!=null)
        {
            String pas=student.getPassword();
            String salt=student.getSalt();
            String re= DigestUtils.md5DigestAsHex(password.getBytes());
            String result=DigestUtils.md5DigestAsHex((re+salt).getBytes());
            if(pas.equals(result))
            {
                httpSession.setAttribute("username",username);
                return "StudentMain";
            }
        }
        model.addAttribute("msg","用户或密码错误，请再次登录");
            return "StudentLogin";

    }
    @RequestMapping("/Query")
    public String ClassQueryById(Model model,HttpSession httpSession,HttpServletRequest httpServletRequest) {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }
          List<ViewClass> listed=classesService.QueryViewClassAll();
          httpSession.setAttribute("ChoosingClasses",listed);
        if(listed!=null) {
            for (ViewClass viewClass : listed) {
                String TeacherID = viewClass.getTeacherID();
                String ClassID = viewClass.getClassID();

                Object k=redisTemplate.opsForValue().get("ClassNumber::"+TeacherID+ClassID);
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
            //httpServletRequest.setAttribute("ClassesListed", listed);
            model.addAttribute("ClassesList", listed);
            return "StudentMainChoose";
        }
        else
        {
            return "StudentMain";
        }
    }
    @RequestMapping("/join")
    public String join(@RequestParam("TeacherID") String TeacherID, @RequestParam("ClassID")
            String ClassID, @RequestParam("Time") String time, HttpSession httpSession,Model model,
                       HttpServletResponse httpServletResponse) throws IOException {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }
        RLock lock=redissonClient.getLock(TeacherID+ClassID);
        int numMax=classesService.queryByID(ClassID).getNumberMax();
        int num=classesService.countNumber(TeacherID,ClassID);

        if(num>=numMax)
        {
            List<ViewClass> viewClasses=classesService.QueryViewClassAll();
            model.addAttribute("msg","该课程人数已经满");
            model.addAttribute("ClassesList", viewClasses);
            lock.unlock();
            return "StudentMainChoose";
        }
        String StudentID=(String) httpSession.getAttribute("username");
        List<class_student> class_students=classesService.QueryClassesByID(StudentID);
        for (management.entity.class_student class_student : class_students)
        {
            Classes classes = classesService.queryByID(class_student.getClassID());
            if(classes==null)
            {
                continue;
            }
            if (ClassesTime.Com(classes.getTime(), time))
            {

                List<ViewClass> viewClasses = classesService.QueryViewClassAll();
                model.addAttribute("msg", "该课程与已存在的" + classes.getClassID() + "号课程存在时间冲突");
                for (ViewClass viewClass : viewClasses)
                {
                    String TeacherIDD = viewClass.getTeacherID();
                    String ClassIDD = viewClass.getClassID();
                    System.out.println(TeacherIDD+" "+ClassIDD);
                    Object k=redisTemplate.opsForValue().get("ClassNumber::"+TeacherID+ClassIDD);
                    if(k!=null)
                    {
                        int numd=Integer.parseInt(k.toString());
                        viewClass.setClassNumber(numd);

                    }
                    else
                    {
                        int numd = classesService.countNumber(TeacherIDD, ClassIDD);
                        viewClass.setClassNumber(numd);

                        redisTemplate.opsForValue().set("ClassNumber::"+TeacherIDD+ClassIDD,numd);
                    }
                }
                model.addAttribute("ClassesList", viewClasses);
                lock.unlock();
                return "StudentMainChoose";
            }
        }
        Boolean f=redisTemplate.hasKey("ViewClass::0");
        if(f!=null)
        {
            redisTemplate.delete("ViewClass::0");
        }
       studentService.JoinClass(StudentID,ClassID,TeacherID);
        redisTemplate.opsForValue().increment("ClassNumber::"+TeacherID+ClassID);
        lock.unlock();
       return  "redirect:/Student/Query";
    }
    @RequestMapping("/delete")
    public String deleteClasses(@RequestParam("TeacherID") String TeacherID, @RequestParam("ClassID")
            String ClassID, HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }
        RLock lock = redissonClient.getLock(TeacherID + ClassID);

            String StudentID = (String) httpSession.getAttribute("username");
            List<class_student> class_students = classesService.QueryClassesByID(StudentID);
            List<Classes> list = CastList.castList(httpSession.getAttribute("list"), Classes.class);
            studentService.deleteClass(StudentID, ClassID, TeacherID);
            model.addAttribute("list", list);
            lock.unlock();
            return "StudentMainChoose";
    }
    @RequestMapping("/QueryChosen")
    public String QueryChosen(HttpSession httpSession,Model model,HttpServletRequest httpServletRequest)
    {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }
        String StudentID=(String) httpSession.getAttribute("username");
        List<Classes> listed=new ArrayList<>();
        List<class_student> list=classesService.QueryClassesByID(StudentID);
        for (int i = 0; i < list.size(); i++)
        {
            Classes classes=classesService.queryByID(list.get(i).getClassID());
            if(classes!=null)
            {
                listed.add(classes);
            }
        }
        model.addAttribute("ChosenClasses",listed);
        httpServletRequest.setAttribute("ChosenClass",listed);
        return "StudentMainChosen";
    }
    @RequestMapping("/deleteChosen")
    public String deleteClass( @RequestParam("ClassID")
            String ClassID, HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }

        String StudentID=(String) httpSession.getAttribute("username");
        String TeacherID=studentService.queryTeacherIDBye(StudentID,ClassID);
        studentService.deleteChosen(ClassID,StudentID);
        redisTemplate.opsForValue().decrement("ClassNumber::"+TeacherID+ClassID);
        return "redirect:/Student/QueryChosen";
    }
    @RequestMapping("/QueryExamPoints")
    public String QueryExamPoints(HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("username")==null)
        {
            model.addAttribute("msg","请先登录");
            return "StudentLogin";
        }
        String StudentID=(String) httpSession.getAttribute("username");
        List<Classes> listed=new ArrayList<>();
        List<class_student> list=classesService.QueryClassesByID(StudentID);
        for (int i = 0; i < list.size(); i++)
        {
            Classes classes=classesService.queryByID(list.get(i).getClassID());
            listed.add(classes);
        }
        model.addAttribute("ChosenClasses",list);
        model.addAttribute("ConcreteClasses",listed);
        return "StudentMainExamPoints";
    }
}
