package club.simplecreate.recommender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
@Component
public class BaseOnContent {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public void setSpecialRecommend(String openId) {
        //删除之前的推荐列表
        redisTemplate.delete("SPECIAL_RECOMMEND:"+openId);
        //如果用户还未浏览任何文章，向他推荐默认文章
        if(redisTemplate.opsForZSet().size("HOBBY:" + openId)==0) {
            Set<Object> topKeyWords= redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
            for(Object keyword: topKeyWords){
                redisTemplate.opsForZSet().unionAndStore("SPECIAL_RECOMMEND:" + openId,"KEYWORD:"+keyword,"SPECIAL_RECOMMEND:" + openId);
            }
        }
        //获得历史记录
        Set<Object> historySet=redisTemplate.opsForZSet().range("HISTORY:"+openId,0,-1);
        //与最新的100篇文章计算相似度，选择前50推荐给用户
        List<Object> newArticleList =redisTemplate.opsForList().range("NEWN",0,99);
        Set<Object> hobbySet=redisTemplate.opsForZSet().reverseRange("HOBBY:"+openId,0,100);
        for(Object articleId:newArticleList){
            double sum=0;
            if(!historySet.contains(articleId)){
                Set<Object> keywordSet=redisTemplate.opsForZSet().reverseRange("ARTICLE_KEYWORDS:"+articleId,0,-1);
                //求交集
                keywordSet.retainAll(hobbySet);
                //求文章相似度
                for(Object keyWord:keywordSet){
                    double a=redisTemplate.opsForZSet().score("HOBBY:"+openId,keyWord);
                    double b=redisTemplate.opsForZSet().score("ARTICLE_KEYWORDS:"+articleId,keyWord);
                    sum+=a*b;
                }
            }
            redisTemplate.opsForZSet().add("SPECIAL_RECOMMEND:"+openId,articleId,sum);
            redisTemplate.opsForZSet().removeRange("SPECIAL_RECOMMEND:"+openId,50,-1);
        }
    }

}
