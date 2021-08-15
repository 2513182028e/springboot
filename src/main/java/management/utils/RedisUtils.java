

package management.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    @Qualifier("re")
    private RedisTemplate<String,Object> redisTemplate;
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public long getExpire(String key) {

        Long d= redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if(d!=null)
        {
            return d;
        }
        else
        {
            return 0;
        }
    }



    public boolean hasKey(String key) {
             if(redisTemplate.hasKey(key).equals(true))
             {
                 return true;
              }
            return  false;
    }



    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


// ============================String=============================


    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }



    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long  d=redisTemplate.opsForValue().increment(key, delta);
        if(d!=null)
        {
            return  d;
        }
        else
        {
            return  0;
        }
    }



    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long d=redisTemplate.opsForValue().increment(key, -delta);
        if(d!=null)
        {
            return  d;
        }
        else
        {
            return  0;
        }
    }


// ================================Map=================================

    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }


    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }



    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }



    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


// ============================set=============================

    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public boolean sHasKey(String key, Object value) {
            Boolean d= redisTemplate.opsForSet().isMember(key, value);
            if(d)
            {
                return true;
            }
            else
            {
                return  false;
            }
    }


    public long sSet(String key, Object... values) {

            Long d= redisTemplate.opsForSet().add(key, values);
            if(d!=null)
            {
                return d;
            }
            return 0;
    }


    public long sSetAndTime(String key, long time, Object... values) {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            if(count!=null)
            {
                return count;
            }
            return 0;
    }


    public long sGetSetSize(String key) {
            Long d= redisTemplate.opsForSet().size(key);
            if(d!=null)
            {
                return d;
            }
            return  0;

    }



    public long setRemove(String key, Object... values) {
        try {
            Long d = redisTemplate.opsForSet().remove(key, values);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

// ===============================list=================================

    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public long lGetListSize(String key) {
            Long d=redisTemplate.opsForList().size(key);
            if(d!=null)
            {
                return d;
            }
            return d;
    }



    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }



    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean lUpdateIndex(String key, long index, Object value)
    {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    public long lRemove(String key, long count, Object value){
            Long d=redisTemplate.opsForList().remove(key,count,value);
            if(d!=null)
            {
                return d;
            }
            else
            {
                return 0;
            }

    }

}
