package club.simplecreate;


import club.simplecreate.pojo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraduationdesignApplicationTests {
  @Autowired
 RedisTemplate<Object,Object> redisTemplate;
 @Test
 void contextLoads() {
   Message res=(Message)redisTemplate.opsForList().index("NEW_FOLLOW_MESSAGES:oUFeq5faEpwmnRwtigXWPY7IxPJU",-3);
  System.out.println(res.getSenderNickname());
 }

}
