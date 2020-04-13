package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class KeyWordCache {

    @Autowired
    private RedisTemplate redisTemplate;
    @Async
    public void increaseKeyWordScore(Set<String> keyWords){
        for(String keyWord:keyWords){
            redisTemplate.opsForZSet().incrementScore("KEYWORD_TOP_N",keyWord,10);
        }
    }
}
