package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public List<Object> getTypeList() {
        return redisTemplate.opsForList().range("TYPE",0,-1);
    }
    public void insertTypeList(List<Object> typeList){
        redisTemplate.opsForList().rightPushAll("TYPE",typeList);
    }
}
