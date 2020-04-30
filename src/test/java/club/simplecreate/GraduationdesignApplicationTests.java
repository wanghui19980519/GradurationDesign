package club.simplecreate;



import club.simplecreate.cache.ArticleCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraduationdesignApplicationTests {
@Autowired
 ArticleCache articleCache;
 @Test
 void contextLoads() {

 }

}
