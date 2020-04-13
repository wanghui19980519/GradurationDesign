package club.simplecreate.cache;

import club.simplecreate.pojo.Message;
import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FollowCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public boolean isFollow(String openId, String userId) {
        //判断作者的关注列表中是否存在该操作用户
        //判断用户的收藏列表中是否存在该articleId
        long res;
        try {
             res=redisTemplate.opsForZSet().rank("FOLLOW_SET:" + openId, userId);
        }catch (Exception e){
            return false;
        }
        if(res>=0){
            return true;
        }else{
            return false;
        }
    }
    public boolean follow(User user, String authorId) {
        //判断该是否已关注
        if(isFollow(user.getOpenid(),authorId)){
            //已关注则取消关注
            long res1=redisTemplate.opsForZSet().remove("FOLLOW_SET:"+authorId,user.getOpenid());
            if(res1==1){
                return true;
            }else{
                return false;
            }
        }
        else{
            //未关注
            //将该用户加入被关注用户的粉丝列表
            boolean res2=redisTemplate.opsForZSet().add("FOLLOW_SET:"+authorId,user.getOpenid(),System.currentTimeMillis());
            //关注信息加入信箱，未读消息加1
            if(res2){
                redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+authorId);
                Message message=new Message(user);
                redisTemplate.opsForList().rightPush("NEW_FOLLOW_MESSAGES:"+authorId,message);
                return true;
            }else {
                return false;
            }

        }

    }
}
