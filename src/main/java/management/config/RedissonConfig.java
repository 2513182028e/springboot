package management.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;

@Configuration
public class RedissonConfig {

    @Bean("ra")
   public RedissonClient redissonClient()
   {
       Config config=new Config();                                                        //Redisosn锁的基本配置
       SingleServerConfig singleServerConfig=config.useSingleServer();
       singleServerConfig.setAddress("redis://192.168.180.139:6379");
       singleServerConfig.setDatabase(0);
       return Redisson.create(config);
   }
}
