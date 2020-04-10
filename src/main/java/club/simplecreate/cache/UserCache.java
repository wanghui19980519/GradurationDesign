package club.simplecreate.cache;

import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserCache{

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    public User selectUserByOpenid(String openid) {
        return (User)redisTemplate.opsForValue().get("USER:"+openid);
    }
    @Async
    public void insertUser(User user){
        redisTemplate.opsForValue().set("USER:"+user.getOpenid(),user);
    }
    @Async
    public void insertHistory(String userId, String articleId) {
        double time=System.currentTimeMillis();
        //先直接去掉，再加入
        redisTemplate.opsForZSet().remove("HISTORY:"+userId,articleId);
        redisTemplate.opsForZSet().add("HISTORY:"+userId,articleId,time);
    }
    @Async
    public void insertArticleKeyWord(String openId, String articleId) {
        //合并用户兴趣列表与文章关键字列表
        redisTemplate.opsForZSet().unionAndStore("HOBBY:"+openId,"ARTICLE_KEYWORDS:"+articleId,"HOBBY:"+openId);
    }

    public Set<Object> getHistoryList(String openId, int page) {
        return redisTemplate.opsForZSet().reverseRange("HISTORY:"+openId,(page-1)*6,page*6-1);
    }

    public long getHistoryListSize(String openId) {
        return redisTemplate.opsForZSet().size("HISTORY:"+openId);
    }

    public Set<Object> getFavoriteList(String openId, int page) {
        return redisTemplate.opsForZSet().reverseRange("FAVORITESET:"+openId,(page-1)*6,page*6-1);
    }

    public long getFavoriteListSize(String openId) {
        return redisTemplate.opsForZSet().size("FAVORITESET:"+openId);
    }

    public long getFollowListSize(String openId) {
        return redisTemplate.opsForZSet().size("FOLLOWSET:"+openId);
    }

    public Set<Object> getFollowList(String openId, int page) {
        return redisTemplate.opsForZSet().reverseRange("FOLLOWSET:"+openId,(page-1)*10,page*10-1);
    }

    public void deleteActionSet(String openId) {
        redisTemplate.delete("ACTION_SET:"+openId);
    }

    public void unionActionSet(String openId) {
        //获得关注列表
        Set<Object> followSet=redisTemplate.opsForZSet().range("FOLLOWSET:"+openId,0,-1);
        //产生动态列表
        for(Object followId:followSet){
            redisTemplate.opsForZSet().unionAndStore("ACTION_SET:"+openId,"USER_ARTICLES:"+followId,"ACTION_SET:"+openId);
        }
    }

    public long getLastReloadTime(String openId) {
        //获得上次刷新时间
        Long res=(Long) redisTemplate.opsForValue().get("LAST_RELOAD_TIME:"+openId);
        //将当前时间设为刷新时间
        setNewLastReloadTime(openId);
        if(res==null){
            return System.currentTimeMillis();
        }
        return res;
    }

    public long getActionSize(String openId,long start, long currentTimeMillis) {
        return redisTemplate.opsForZSet().count("ACTION_SET:"+openId,start,currentTimeMillis);
    }
    public void setNewLastReloadTime(String openId){
        redisTemplate.opsForValue().set("LAST_RELOAD_TIME:"+openId,System.currentTimeMillis());
    }

    public Set<Object> getActionList(String openId, int page) {
        return redisTemplate.opsForZSet().reverseRange("ACTION_SET:"+openId,(page-1)*6,page*6-1);
    }

    public long getActionListSize(String openId) {
        return redisTemplate.opsForZSet().size("ACTION_SET:"+openId);
    }
}
