package club.simplecreate.search;

import club.simplecreate.utils.Ansj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Component
public class KeyWordSearch {

    @Autowired
    private RedisTemplate redisTemplate;
    @SuppressWarnings("unchecked")
    public Set<Integer> getResult(String str){
        Set<String> target=Ansj.split(str);
        //将搜索语句分词，然后将分解出对应关键字的倒排索引进行合并
        redisTemplate.opsForZSet().unionAndStore(" ",target,"SEARCH_HISTORY:"+str);

        //设置过期时间1分钟
        redisTemplate.expire("SEARCH_HISTORY:"+str,10,TimeUnit.MINUTES);

        //返回所有结果
        return redisTemplate.opsForZSet().range("SEARCH_HISTORY:"+str,0,-1);
    }
}
