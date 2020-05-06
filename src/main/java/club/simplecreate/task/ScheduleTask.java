package club.simplecreate.task;

import club.simplecreate.controller.WebSocket;
import club.simplecreate.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ScheduleTask {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    /**
     * 每隔1分钟执行一次任务，循环为在线人数查看消息队列中有无新消息
     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void sendMessage(){
//        Map<String, WebSocket> map = WebSocket.webSocketSet;
//        for(Map.Entry<String, WebSocket> entry:map.entrySet()){
//            //从redis中取出新消息数量
//            int nums=0;
//            if(redisTemplate.hasKey("NEW_MESSAGE_NUMS:"+entry.getKey())) {
//                nums = (int) redisTemplate.opsForValue().get("NEW_MESSAGE_NUMS:" + entry.getKey());
//            }
//                entry.getValue().sendMessage(nums);
//        }
//    }

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

    //@Scheduled(cron = "0 0 0 */1 * ?")
    /*
    public void attenuationHeat(){

        //裁剪热词列表
        redisTemplate.opsForZSet().removeRange("KEYWORD_TOP_N",100,-1);
        //减低列表热词热度，每天热度*0.8
        Set<ZSetOperations.TypedTuple<Object>> hotKeywords=redisTemplate.opsForZSet().reverseRangeWithScores("KEYWORD_TOP_N",0,-1);
        for(ZSetOperations.TypedTuple<Object> hotKeyword :hotKeywords){
            redisTemplate.opsForZSet().incrementScore("KEYWORD_TOP_N",hotKeyword.getValue(),hotKeyword.getScore()*-0.2);
        }

        //获取用户列表
        Set<Object> userSet=redisTemplate.keys("USER:"+"*");
        //获取用户的兴趣列表
        for(Object user:userSet){
            String temp=(String) user;
            String userId=temp.split(":");
            //裁剪用户兴趣列表
            //取出排第一百个兴趣关键字的权值
            //因为zset没有根据索引获取值的方法，zset不能直接裁剪索引，必须借助排第一百个关键字的分值
            Set<ZSetOperations.TypedTuple<Object>>  res=redisTemplate.opsForZSet().reverseRangeByScoreWithScores("HOBBY:"+userId,100,100);
            if(res!=null){
                int hobbyScore=0;
                for(ZSetOperations.TypedTuple<Object> keyword:res){
                    hobbyScore+=keyword.getScore();
                }
                redisTemplate.opsForZSet().removeRangeByScore("HOBBY:"+userId,0,hobbyScore);
            }
            //裁剪用户的历史记录（每个人保存最近访问的前100条历史记录就可以了）
            res=redisTemplate.opsForZSet().reverseRangeByScoreWithScores("HISTORY:"+userId,100,100);
            if(res!=null){
                //第一百个历史记录的时间
                int historyScore=0;
                for(ZSetOperations.TypedTuple<Object> keyword:res){
                    historyScore+=keyword.getScore();
                }
                redisTemplate.opsForZSet().removeRangeByScore("HISTORY:"+userId,0,historyScore);
            }
            //对用户兴趣列表进行衰减
            Set<ZSetOperations.TypedTuple<Object>> hobbyKeywords=redisTemplate.opsForZSet().reverseRangeWithScores("HOBBY:"+userId,0,-1);
            for(ZSetOperations.TypedTuple<Object> hobbyKeyword :hobbyKeywords){
                redisTemplate.opsForZSet().incrementScore("KEYWORD_TOP_N",hobbyKeyword.getValue(),hobbyKeyword.getScore()*-0.2);
            }
        }
        //将redis中的需要保存的记录存起来（收藏数据比较重要需要保存），需要借助一个上次保存时间
        //收藏数据是用户每个用户保存一个收藏文章列表
    }
    */

    @Scheduled(cron = "0 0 0 */1 * ?")
    public void generateUserSimilarity(){
        //从数据库中获得所有用户id
        List<String> userIdList= userMapper.getAllUserId();
        //先清除当前保留的用户相似度列表
        for(int i=0;i<userIdList.size();i++){
            redisTemplate.delete("USER_SIMILARITY:"+userIdList.get(i));
        }
        //利用缓存中用户点赞过的文章计算用户相似度
        for(int i=0;i<userIdList.size();i++){
            //获得用户1喜欢文章的数量
            long length1=redisTemplate.opsForZSet().size("HISTORY:"+userIdList.get(i));
            for(int j=i+1;j<userIdList.size();j++){
                //利用redis的有序集合的求交集，获得交集的长度即为俩个用户共同浏览过的文章数
                long total=redisTemplate.opsForZSet().intersectAndStore(
                        "HISTORY:"+userIdList.get(i),
                        "HISTORY:"+userIdList.get(j),
                        "temp:"+userIdList.get(i)+"+"+userIdList.get(j));
                redisTemplate.delete("temp:"+userIdList.get(i)+"+"+userIdList.get(j));
                //获得用户2喜欢的文章数量
                long length2=redisTemplate.opsForZSet().size("HISTORY:"+userIdList.get(j));
                //计算相似度，余弦公式(不带惩罚措施)
                double similarity=0;
                if(total!=0&&length1!=0&&length2!=0){
                    similarity=total/(Math.sqrt(length1*length2));
                }
                //将该相似度加入俩者的相似度列表
                redisTemplate.opsForZSet().add("USER_SIMILARITY:"+userIdList.get(i),userIdList.get(j),similarity);
                redisTemplate.opsForZSet().add("USER_SIMILARITY:"+userIdList.get(j),userIdList.get(i),similarity);
            }
        }
    }
}
