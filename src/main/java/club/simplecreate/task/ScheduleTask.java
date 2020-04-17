package club.simplecreate.task;

import club.simplecreate.controller.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Set;

@Service
public class ScheduleTask {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 每隔1分钟执行一次任务，循环为在线人数查看消息队列中有无新消息
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

    /**
     * 每小时更新默认推荐列表
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void generateDefaultRecommendList(){
        //删除之前的默认推荐列表
        redisTemplate.delete("DEFAULT_RECOMMEND_LIST");
        //通过最新的前十大热词生成最新的默认推荐列表
        Set<Object> topKeyWords= redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
        for(Object keyword: topKeyWords){
            redisTemplate.opsForZSet().unionAndStore("DEFAULT_RECOMMEND_LIST","KEYWORD:"+keyword,"DEFAULT_RECOMMEND_LIST");
        }
    }
    /**
     * 每天衰减热词热度，和用户兴趣热度
     * Attenuation heat
     */
//    @Scheduled(cron = "0 0 0 */1 * ?")
//    public void attenuationHeat(){
//        //裁剪热词列表
//
//        //减低列表热词热度
//
//        //获取用户列表
//
//        //获取用户的兴趣列表
//
//        //裁剪用户兴趣列表
//
//        //对用户兴趣列表进行衰减

           //裁剪用户历史记录

           //将redis中的需要保存的记录存起来
//    }
}
