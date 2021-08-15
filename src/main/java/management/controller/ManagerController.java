package management.controller;


import management.entity.Manager;
import management.entity.Student;
import management.entity.Teacher;
import management.service.ManagerServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequestMapping("/Manager")
public class ManagerController {

    @Autowired
    ManagerServiceImpl managerService;

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/login")
    public String Login()
    {
        return "ManagerLogin";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession httpSession,Model model)
    {
        httpSession.removeAttribute("ManagerID");
        return "index";
    }
    @RequestMapping("/login/Authentication")
    public String Authentication(HttpServletRequest httpServletRequest, HttpSession httpSession,Model model)
    {

        if(httpSession.getAttribute("ManagerID")!=null)
        {
            return "ManagerMain";
        }
        String username=httpServletRequest.getParameter("username");
        String password=httpServletRequest.getParameter("password");
        Manager manager=managerService.queryManagerByID(username);
        if(manager!=null)
        {
            String ps=manager.getPassword();
            if(ps.equals(password))
            {
                httpSession.setAttribute("ManagerID",username);
                return "ManagerMain";
            }
        }

        model.addAttribute("msg","用户或密码错误，请再次登录");
        return "ManagerLogin";
    }
    @RequestMapping("/Students")
    public String StudentController(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        List<Student> students=managerService.queryStudentALL();
        model.addAttribute("students",students);
        return "ManagerMainStudent";
    }

    @RequestMapping("/deleteStudent")
    public String DeleteStudent(Model model, @RequestParam("StudentID") String StudentID,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        managerService.deleteStudent(StudentID);
        Boolean b=redisTemplate.hasKey("Student::0");
        if(b!=null)
        {
            redisTemplate.delete("Student::0");
        }
        return "redirect:/Manager/Students";
    }


    @RequestMapping("/updateStudent")
    public String UpdateStudent(Model model,@RequestParam("StudentID") String StudentID,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        Student student=managerService.queryStudent(StudentID);
        model.addAttribute("student",student);
        return "ManagerMainStudentUpdate";
    }

    @RequestMapping("/Students/UpdateOver")
    public String UpdateOver(@RequestParam("StudentID") String StudentID,
                             @RequestParam("Password") String Password,
                             @RequestParam("Name") String Name,
                             @RequestParam("Major") String Major
    ,@RequestParam("College") String College,@RequestParam("Age")  String Age,
                             @RequestParam("Sex") String Sex,
                             @RequestParam("Salt") String Salt,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        if(StudentID.equals("")||Password.equals("")||Name.equals("")||Major.equals("")||College.equals("")||
        Age.equals("")||Sex.equals(""))
        {
            Student student=managerService.queryStudent(StudentID);
            model.addAttribute("student",student);
            model.addAttribute("msg","请输入完整信息");
            return "ManagerMainStudentUpdate";
        }
        Student student=new Student();
        student.setStudentID(StudentID);
        student.setPassword(Password);
        student.setName(Name);
        student.setMajor(Major);
        student.setCollege(College);
        student.setAge(Integer.parseInt(Age));
        student.setSex(Sex);
        student.setSalt(Salt);
        managerService.updateStudent(student);
        Boolean b=redisTemplate.hasKey("Student::0");
        if(b!=null)
        {
            redisTemplate.delete("Student::0");
        }
        return "redirect:/Manager/Students";
    }
    @RequestMapping("/AddStudent")
    public String AddStudent(HttpSession httpSession,Model model)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        return "ManagerMainStudentAdd";
    }


    @RequestMapping("/Students/AddOver")
    public String StudentAddOver(@RequestParam("StudentID") String StudentID,
                                 @RequestParam("Password") String Password,
                                 @RequestParam("Name") String Name,
                                 @RequestParam("Major") String Major
            , @RequestParam("College") String College, @RequestParam("Age")  String Age,
                                 @RequestParam("Sex") String Sex,
                                 @RequestParam("Salt") String Salt, HttpServletResponse httpServletResponse,
                                 HttpServletRequest httpServletRequest,Model model,HttpSession httpSession) throws UnsupportedEncodingException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }


        if(StudentID.equals("")||Password.equals("")||Name.equals("")||Major.equals("")||College.equals("")||
                Age.equals("")||Sex.equals(""))
        {
            model.addAttribute("msg","请输入完整信息");
            return "ManagerMainStudentAdd";
        }
        Student student=new Student();
        student.setStudentID(StudentID);
        student.setPassword(Password);
        student.setName(Name);
        student.setMajor(Major);
        student.setCollege(College);
        student.setAge(Integer.parseInt(Age));
        student.setSex(Sex);
        student.setSalt(Salt);
        managerService.addStudent(student);
        Boolean b=redisTemplate.hasKey("Student::0");
        if(b!=null)
        {
            redisTemplate.delete("Student::0");
        }
        return "redirect:/Manager/Students";
    }

    @RequestMapping("/Teachers")
    public String TeacherQuery(Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        List<Teacher> teachers=managerService.queryTeacherAll();
        model.addAttribute("TeacherS",teachers);
        return "ManagerMainTeacher";
    }


    @RequestMapping("/deleteTeacher")
    public String DeleteTeacher(Model model, @RequestParam("TeacherID") String TeacherID,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        managerService.deleteTeacher(TeacherID);
        Boolean b=redisTemplate.hasKey("Teacher::0");
        if(b!=null)
        {
            redisTemplate.delete("Teacher::0");
        }
        return "redirect:/Manager/Teachers";
    }


    @RequestMapping("/updateTeacher")
    public String UpdateTeacher(Model model,@RequestParam("TeacherID") String TeacherID,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        Teacher teacher=managerService.queryTeacher(TeacherID);
        //System.out.println(student);
        model.addAttribute("Teacher",teacher);
        return "ManagerMainTeacherUpdate";
    }

    @RequestMapping("/Teachers/UpdateOver")
    public String updateTeacherOver(@RequestParam("TeacherID") String TeacherID,
                                    @RequestParam("TeacherName") String TeacherName,
                                    @RequestParam("Password") String Password,
                                    @RequestParam("Major") String Major,
                                    @RequestParam("College") String College,
                                    @RequestParam("Salt") String Salt,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        if(TeacherID.equals("")||TeacherName.equals("")||Password.equals("")||Major.equals("")||
        College.equals(""))
        {
            Teacher teacher=managerService.queryTeacher(TeacherID);
            //System.out.println(student);
            model.addAttribute("Teacher",teacher);
            model.addAttribute("msg","请输入完整信息");
            return "ManagerMainTeacherUpdate";
        }
        Teacher teacher=new Teacher();
        teacher.setTeacherID(TeacherID);
        teacher.setPassword(Password);
        teacher.setTeacherName(TeacherName);
        teacher.setMajor(Major);
        teacher.setCollege(College);
        teacher.setSalt(Salt);
        managerService.updateTeacher(teacher);
        Boolean b=redisTemplate.hasKey("Teacher::0");
        if(b!=null)
        {
            redisTemplate.delete("Teacher::0");
        }
        return "redirect:/Manager/Teachers";
    }

    @RequestMapping("/AddTeacher")
    public String AddTeacher(HttpSession httpSession,Model model)

    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        return "ManagerMainTeacherAdd";
    }


    @RequestMapping("/Teachers/AddOver")
    public String TeacherAddOver(@RequestParam("TeacherID") String TeacherID,
                                 @RequestParam("TeacherName") String TeacherName,
                                 @RequestParam("Password") String Password,
                                 @RequestParam("Major") String Major,
                                 @RequestParam("College") String College,
                                 @RequestParam("Salt") String Salt,Model model,HttpSession httpSession)
    {
        if(httpSession.getAttribute("ManagerID")==null)
        {
            model.addAttribute("msg","请先登录");
            return "ManagerLogin";
        }
        if(TeacherID.equals("")||TeacherName.equals("")||Password.equals("")||Major.equals("")||
                College.equals(""))
        {

            model.addAttribute("msg","请输入完整信息");
            return "ManagerMainTeacherAdd";
        }
        Teacher teacher=new Teacher();
        teacher.setTeacherID(TeacherID);
        teacher.setPassword(Password);
        teacher.setTeacherName(TeacherName);
        teacher.setMajor(Major);
        teacher.setCollege(College);
        teacher.setSalt(Salt);
        managerService.addTeacher(teacher);
        Boolean b=redisTemplate.hasKey("Teacher::0");
        if(b!=null)
        {
            redisTemplate.delete("Teacher::0");
        }
        return "redirect:/Manager/Teachers";
    }




}
