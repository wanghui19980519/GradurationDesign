package club.simplecreate.recommender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class UserCF {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public void setSpecialRecommend(String openId){
        //删除之前的推荐列表
        redisTemplate.delete("SPECIAL_RECOMMEND:"+openId);
        //获得历史记录
        Set<Object> historySet=redisTemplate.opsForZSet().range("HISTORY:"+openId,0,-1);
        //获得与其兴趣相似度最高的前五名的id，及其相似度
        Set<ZSetOperations.TypedTuple<Object>> userIdAndSimilaritys=redisTemplate.opsForZSet().reverseRangeWithScores("USER_SIMILARITY:"+openId,0,4);
        for(ZSetOperations.TypedTuple<Object> userIdAndSimilarity : userIdAndSimilaritys){
            if (userIdAndSimilarity.getScore()>0) {
                //先获得该用户浏览文章列表
                Set<Object> articleIds = redisTemplate.opsForZSet().range("HISTORY:" + userIdAndSimilarity.getValue(),0,-1);
                for (Object articleId : articleIds) {
                    if (!historySet.contains(articleId)) {
                        //加入推荐列表
                        redisTemplate.opsForZSet().incrementScore("SPECIAL_RECOMMEND:" + openId, articleId, userIdAndSimilarity.getScore());
                    }
                }
            }
        }
    }
}
