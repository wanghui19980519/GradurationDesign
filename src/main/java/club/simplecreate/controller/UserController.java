package club.simplecreate.controller;

import club.simplecreate.pojo.User;
import club.simplecreate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping("/wxLogin")
    public User login(User user){
        System.out.println(user.toString());
        return userService.wxLogin(user);
    }
    @ResponseBody
    @RequestMapping("/checkLogin")
    public Map<String,Object> checkLogin(String token){
        return userService.checkLogin(token);
    }

}
