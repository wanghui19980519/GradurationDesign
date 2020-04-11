package club.simplecreate.task;

import club.simplecreate.controller.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ScheduleTask {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 每隔1分钟执行一次任务
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void sendMessage(){
        Map<String, WebSocket> map = WebSocket.webSocketSet;
        for(Map.Entry<String, WebSocket> entry:map.entrySet()){
            //从redis中取出新消息数量
            int nums=0;
            if(redisTemplate.hasKey("NEW_MESSAGE_NUMS:"+entry.getKey())) {
                nums = (int) redisTemplate.opsForValue().get("NEW_MESSAGE_NUMS:" + entry.getKey());
            }
            if(nums>0){
                entry.getValue().sendMessage(nums);
            }
        }
    }
}
