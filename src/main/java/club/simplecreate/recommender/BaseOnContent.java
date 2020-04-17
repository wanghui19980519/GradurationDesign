package club.simplecreate.recommender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class BaseOnContent {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public void setSpecialRecommend(String openId) {
        //删除之前的推荐列表
        redisTemplate.delete("SPECIAL_RECOMMEND:"+openId);
        //如果用户还未浏览任何文章，向他推荐默认文章
        if(redisTemplate.opsForZSet().size("HOBBY:" + openId)<=10) {
            Set<Object> topKeyWords= redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
            for(Object keyword: topKeyWords){
                redisTemplate.opsForZSet().unionAndStore("SPECIAL_RECOMMEND:" + openId,"KEYWORD:"+keyword,"SPECIAL_RECOMMEND:" + openId);
            }
        }
        //取出前20个兴趣关键字及权重
        Set<ZSetOperations.TypedTuple<Object>> keywords= redisTemplate.opsForZSet().reverseRangeByScoreWithScores("HOBBY:" + openId, 0, 19);
        for (ZSetOperations.TypedTuple<Object> keyword : keywords) {
            //取出关键字前十的文章及权重
            Set<ZSetOperations.TypedTuple<Object>> articles = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("KEYWORD:" + keyword.getValue(), 0, 19);
            //相乘的出文章相应权重
            for (ZSetOperations.TypedTuple<Object> article : articles) {
                //获得历史记录
                Set<Object> historySet=redisTemplate.opsForZSet().range("HISTORY:"+openId,0,-1);
                //过滤已经看过的文章
                if(!historySet.contains(article.getValue())) {
                    redisTemplate.opsForZSet().incrementScore("SPECIAL_RECOMMEND:" + openId, article.getValue(), keyword.getScore() * article.getScore());
                }
            }
        }
    }

}
