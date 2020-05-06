package club.simplecreate.cache;

import club.simplecreate.controller.WebSocket;
import club.simplecreate.pojo.Message;
import club.simplecreate.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FollowCache {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    public boolean isFollow(String openId, String authorId) {
        //判断操作用户的关注列表中是否存在该作者
        long res;
        try {
             res=redisTemplate.opsForZSet().rank("FOLLOW_SET:" +openId,authorId);
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
            long res1=redisTemplate.opsForZSet().remove("FOLLOW_SET:"+user.getOpenid(),authorId);
            if(res1==1){
                return true;
            }else{
                return false;
            }
        }
        else{
            //未关注
            //将该用户加入被关注用户的粉丝列表
            boolean res2=redisTemplate.opsForZSet().add("FOLLOW_SET:"+user.getOpenid(),authorId,System.currentTimeMillis());
            //关注信息加入信箱，未读消息加1
            if(res2){
                redisTemplate.opsForValue().increment("NEW_MESSAGE_NUMS:"+authorId);
                Message message=new Message(user);
                redisTemplate.opsForList().rightPush("NEW_FOLLOW_MESSAGES:"+authorId,message);
                //判断用户是否在线，在线直接向其发送通知
                Map<String, WebSocket> map = WebSocket.webSocketSet;
                if(map.containsKey(authorId)){
                    map.get(authorId).sendMessage(1);
                }
                return true;
            }else {
                return false;
            }

        }

    }


}
