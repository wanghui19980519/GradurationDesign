package club.simplecreate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(value = {"club.simplecreate.dao"})
@SpringBootApplication
public class GraduationdesignApplication {

    public static void main(String[] args) {
    SpringApplication.run(GraduationdesignApplication.class, args);
}

}
