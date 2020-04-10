package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
public class TokenCache {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public void setToken(String token,String openid){
        redisTemplate.opsForValue().set("TOKEN:"+token,openid);
        redisTemplate.expire("TOKEN:"+token,1, TimeUnit.HOURS);
    }
    public String getToken(String token){
        return (String) redisTemplate.opsForValue().get("TOKEN:"+token);
    }
    public void setTokenTTL(String token){
        redisTemplate.expire("TOKEN:"+token,1, TimeUnit.HOURS);
    }

}
