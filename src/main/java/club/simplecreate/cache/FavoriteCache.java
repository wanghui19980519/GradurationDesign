package club.simplecreate.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FavoriteCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public boolean isFavorite(String openId, String articleId) {
        //判断用户的收藏列表中是否存在该articleId
        long res;
        try{
          res=redisTemplate.opsForZSet().rank("FAVORITESET:"+openId,articleId);
        }catch (Exception e){
            return false;
        }
        if(res>=0){
            return true;
        }else{
            return false;
        }
    }
    public boolean favorite(String openId, String articleId) {
        if(isFavorite(openId,articleId)){
            //已收藏则取消关注
            long res=redisTemplate.opsForZSet().remove("FAVORITESET:"+openId,articleId);
            if (res==1){
                return true;
            }else{
                return false;
            }
        }
        else{
            //未收藏
            //将该文章加入用户的收藏列表
            boolean res=redisTemplate.opsForZSet().add("FAVORITESET:"+openId,articleId,System.currentTimeMillis());
            if(res){
                return true;
            }else {
                return false;
            }
        }
    }
}
