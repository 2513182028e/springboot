   #：功能描述
    ##学生选课系统分为三个模块
      ###管理员模块：
            ####（1）实现对于学生用户的所有权限，个人信息的修改，增加，删除，查询功能；实现对于教师个人信息的修改，增加，删除，查询功能；
            ####（2）实现对于教师任课关系的修改，增加，删除，查询
            ####（3）密码的修改以及课程信息的全部权限
      ###教师模块：
            ####（1）查询自己的任教课程信息，对学生各门成绩进行打分
      ###学生模块：
            ####（1）完成选课功能，退课，以及成绩的查询
            
  #：开发环境
     ##IDEA（专业版）2020.3，Linux（开启Redis缓存，以及Redisson分布式锁）
     ##NavicatPremium12            
  #：涉及的技术
    ##前端：html
    ##模板引擎：thymeleaf
    ##后端：sprongboot+mybatis
    ##缓存：redis
    ##分布式控制：Redisson锁
    ##数据库：mysql，redis
   #：设计思路
  前提：
   实体类中（entity）存在两个类，一个是课程类Classes，其属性包括（ private String ClassID;
    private String ClassName;
    private String Context;
    private String ClassDurationPoints（课程平时分占比）;
    private String ClassFinalPoints（课程期末分占比）;
    private String ClassPoints;
    private  int ClassNumber;
    private  int NumberMax;
    private String Place;
    private String Time）
    还有一个任课关系类class_teacher其属性
    (private String TeacherID;
    private String ClassID;)
    两个类合并成显示类ViewClass（既在Classes类基础是加入了TeacherID属性）
   当学生登录成功（密码采用了MD5加密处理）后，进入的主界面便是分为三个模块：学生选课，已选课查询，成绩查询
   学生选课界面从数据库中由Class表和class——teacher表联合查询而来，用户可在此页面进行选课操作

   同时在选课的同时采取了Redisson锁，保证了并发条件下的数据一致性也避免了每个进程加的锁被其他进程释放的情况，也避免了程序运行过程中出现的奔溃导致出现死锁的情况，
   在选课操作中使用了自己的工具类，判断该选课与以前的课程是否存在时间冲突的情况（实现情况，可见代码的工具类）
   ，若存在，则返回选课失败的信息，课程时间AZ05141 A到Z分别代表第一周到第26周，
   第三位（数字为） 0代表单周，1代表双周，3代表每周 
   第四位 1到5 代表周1到周5
   第五位 1到6 代表第几节课               
   第六位与第三位意义相同
   第七为与第五位意义相同  
   AZ05141   第1周--第26周 单周 一节课在周五5 第一节课  另外一节课在周4 ，第一节课
   已选课查询，则从数据库中的class——student查询而来，其属性 
   private String StudentID;
    private String ClassID;
    private String TeacherIDD;
    private int ClassDurationPoints（课程平时成绩）;
    private int ClassFinalPoints（课程期末卷面成绩）;
    private int ClassPoints（课程最终成绩）;
    成绩查询模块则同时显示已经选的课程信息（Class类和thymeleaf组成）和已选课的表（由class——student和thymeleaf组成）
    
    #教师模块 
      ##教师模块较为简单，主要就两个功能 
      ###1：查询自己所教的课
         在显示ViewClass（类）时，将TeacherID等于自己ID号的类选择出来，再返回给前端显示即可
      ###2：给选了自己课的学生打分
         class——student类中包括教师编号(TeacherID)，学生编号（StudentID）和课程信息编号（ClassID），因此可以查询出选了该教师的选课关系，再每个选课关系后附带两个连接
         1：打分 点击后跳转至打分页面，点击提交后返回至后端处理
         2：撤销打分的，自动将分数归零，后返回至后端处理
     #管理员模块
       ##1：学生个人信息的增删改查
         2：教师个人信息的增删改查
         3：课程信息的增删改查
         4: 教师任课信息的增删改查
     #缓存方面
         由于Springboot和redis进行了组合，使用了注解来缓存，
         考虑到此系统为学生选课系统，故管理员在信息方面的修改较为少因此管理员在ManagerClassServiceImpl（管理员管理课程与任教关系）层中的查询语句都使用了@Cacheable注解，
         插入语句采用了@CachePut，删除语句采用了@CacheEvit，更新采用了@CachePut，
         同理管理员在教师信息方面的修改，也较少，故也采用了上述相同的处理方法，其中在ViewClass类中一个重要属性，（在缓存中键为ViewClass：：0，代表所以的选课信息）便是（ClassNumber）课程目前已参加的人数，
         无论是管理员，教师，还是学生登录时，正是选课阶段，因此这个属性不断变化，如果每次都从MYSQL数据库中查询，虽然在查找时建立了主键和索引，
         查找顺序采用了最左匹配原则进可能的提高查询速度，但速度依然不够，因此该属性必须单独存储到redis中，在Redis缓存中创建键为：ClassNumber：：+TeaherID+ClassID的键值对，
         每当学生加入特定的课程时，利用redis中的incr与decr来进行人数的增加与减少，这样便避免了去数据库中的频繁查找
         同时对于查询时，数据库中第一次查询不到数据时，也将此查询条件存入Redis数据库，进可能避免了缓存穿透的问题
         如果管理员需要修改课程，教师，课程信息或者教师的任教信息时，在每次修改（这里指增删改）后都会将相关的缓存主动删除，避免缓存中的数据与MYSQL数据库中的不一致
         例如：Redis缓存中存储的键为ViewClass：：0的数据，其本质由MYSQL数据库中的Class表和class——teacher表依靠同一属性TeacherID连接而成，因此，在管理员对课程信息（Class）或者
         任教关系（Class——teacher）进行增删改完成之后，都需要将ViewClass：：0这个键主动删除，其他的缓存实现思路同理，具体可见代码（含注释）
        
         
         
           
    
    
    
    
    
    
    
    
    
    
    
    
