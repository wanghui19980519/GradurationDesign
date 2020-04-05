package club.simplecreate;

import club.simplecreate.cache.CommentCache;
import club.simplecreate.cache.UserCache;

import club.simplecreate.pojo.Like;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GraduationdesignApplicationTests {
   @Autowired
    UserCache userCache;
    @Autowired
    CommentCache commentCache;
    @Test
    void contextLoads() {

    }

}
