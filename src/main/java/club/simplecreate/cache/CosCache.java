package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CosCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public String getSecretId() {
        return (String) redisTemplate.opsForValue().get("COS_SecretId");
    }

    public String getSecretKey() {
        return (String) redisTemplate.opsForValue().get("COS_SecretKey");
    }
}
