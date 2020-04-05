package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FavoriteCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public boolean favorite(String openId, String articleId) {
        long res;
        //判断该是否已收藏
        if(redisTemplate.opsForSet().isMember("FAVORITESET:"+openId,articleId)){
            //已收藏则取消关注
            res=redisTemplate.opsForSet().remove("FAVORITESET:"+openId,articleId);
        }
        else{
            //未收藏
            //将该文章加入用户的收藏列表
            res=redisTemplate.opsForSet().add("FAVORITESET:"+openId,articleId);
        }
        if(res==1){
            return true;
        }else{
            return false;
        }
    }
}
