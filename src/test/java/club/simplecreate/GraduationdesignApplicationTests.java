package club.simplecreate;



import club.simplecreate.cache.ArticleCache;
import club.simplecreate.pojo.Article;
import club.simplecreate.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraduationdesignApplicationTests {
 @Autowired
 private RedisTemplate<Object,Object> redisTemplate;
@Autowired
private ArticleCache articleCache;
 @Autowired
 private ArticleService articleService;
 @Test
 void contextLoads() {

 }

}
