package club.simplecreate.cache;

import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCache{

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    public User selectUserByOpenid(String openid) {
        return (User)redisTemplate.opsForValue().get("USER:"+openid);
    }
    public void insertUser(User user){
        redisTemplate.opsForValue().set("USER:"+user.getOpenid(),user);
    }

    public void insertHistory(String articleId, String userId) {
        double time=System.currentTimeMillis();
        redisTemplate.opsForZSet().add("HISTORY"+userId,articleId,time);
    }

    public void insertArticleKeyWord(String openId, String articleId) {
        //合并用户兴趣列表与文章关键字列表
        redisTemplate.opsForZSet().unionAndStore("HOBBY:"+openId,"KEYWORDS:"+articleId,"HOBBY:"+openId);
    }

    public boolean isLike(String openId, String articleId) {
        //判断用户的点赞列表中是否存在该articleId
        return redisTemplate.opsForSet().isMember("LIKESET:"+openId,articleId);
    }

    public boolean isFavorite(String openId, String articleId) {
        //判断用户的收藏列表中是否存在该articleId
        return redisTemplate.opsForSet().isMember("FAVORITESET:"+openId,articleId);
    }

    public boolean isFollow(String openId, String userId) {
        //判断作者的关注列表中是否存在该操作用户
        return redisTemplate.opsForSet().isMember("FOLLOWSET:"+userId,openId);
    }


}
