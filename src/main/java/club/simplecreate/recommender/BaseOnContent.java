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
        //如果用户还未浏览任何文章，向他推荐默认文章
        if(redisTemplate.opsForZSet().size("HOBBY:" + openId)==0) {
            Set<Object> topKeyWords= redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
            for(Object keyword: topKeyWords){
                redisTemplate.opsForZSet().unionAndStore("SPECIAL_RECOMMEND:" + openId,"KEYWORD:"+keyword,"SPECIAL_RECOMMEND:" + openId);
            }
            return;
        }
        //获得历史记录
        Set<Object> historySet=redisTemplate.opsForZSet().range("HISTORY:"+openId,0,-1);
        //与最新的100篇文章计算相似度，选择前50推荐给用户
        List<Object> newArticleList =redisTemplate.opsForList().range("NEWN",0,99);
        //取出前100兴趣关键字
        Set<Object> hobbySet=redisTemplate.opsForZSet().reverseRange("HOBBY:"+openId,0,19);
        for(Object articleId:newArticleList){
            double sum=0;
            double sum1=0;
            double sum2=0;
            if(!historySet.contains(articleId)){
                Set<Object> keywordSet=redisTemplate.opsForZSet().reverseRange("ARTICLE_KEYWORDS:"+articleId,0,19);
                //求交集
                keywordSet.addAll(hobbySet);
                //求文章相似度
                for(Object keyWord:keywordSet){
                    double a=0,b=0;
                    if(redisTemplate.opsForZSet().score("HOBBY:"+openId,keyWord)!=null){
                        a=redisTemplate.opsForZSet().score("HOBBY:"+openId,keyWord);
                    }
                    if(redisTemplate.opsForZSet().score("ARTICLE_KEYWORDS:"+articleId,keyWord)!=null){
                        b=redisTemplate.opsForZSet().score("ARTICLE_KEYWORDS:"+articleId,keyWord);
                    }
                    sum1+=a*a;
                    sum2+=b*b;
                    sum+=a*b;
                }
                //余弦相似度
                double sim=sum/ Math.sqrt(sum1*sum2);
                redisTemplate.opsForZSet().incrementScore("SPECIAL_RECOMMEND:"+openId,articleId,sim);
                redisTemplate.opsForZSet().removeRangeByScore("SPECIAL_RECOMMEND:"+openId,0,0);
            }
        }
    }

}
