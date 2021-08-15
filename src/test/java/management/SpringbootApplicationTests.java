package management;

import management.dao.StudentDao;
import management.service.StudentServiceImpl;
import management.utils.GetSalt;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import sun.security.provider.MD5;

import javax.xml.ws.soap.Addressing;
import java.util.Set;

@SpringBootTest
class SpringbootApplicationTests {


    @Autowired
    @Qualifier("re")
    private  RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {

        for(int i=0;i<10;i++)
        {
           String h=GetSalt.getSalt(8);
           System.out.println("盐值:"+h);
           String result= DigestUtils.md5DigestAsHex("123".getBytes());
           System.out.println(result);
           String re=DigestUtils.md5DigestAsHex((result+h).getBytes());
           System.out.println("结果："+re);

        }
    }

}