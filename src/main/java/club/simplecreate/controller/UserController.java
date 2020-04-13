package club.simplecreate.controller;

import club.simplecreate.pojo.User;
import club.simplecreate.service.ArticleService;
import club.simplecreate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ArticleService articleService;

    @ResponseBody
    @RequestMapping("/wxLogin")
    public User login(User user){
        return userService.wxLogin(user);
    }
    @ResponseBody
    @RequestMapping("/checkLogin")
    public Map<String,Object> checkLogin(String token){
        return userService.checkLogin(token);
    }

    @ResponseBody
    @RequestMapping("/getHistoryList")
    public Map<String,Object> getHistoryList(HttpServletRequest request,int page){
        String openId=(String)request.getAttribute("userId");
        return articleService.getHistoryList(openId,page);
    }
    @ResponseBody
    @RequestMapping("/getFavoriteList")
    public Map<String,Object> getFavoriteList(HttpServletRequest request,int page){
        String openId=(String)request.getAttribute("userId");
        return articleService.getFavoriteList(openId,page);
    }
    @ResponseBody
    @RequestMapping("/getFollowList")
    public List<User> getFollowList(HttpServletRequest request){
        String openId=(String)request.getAttribute("userId");
        return userService.getFollowList(openId);
    }
    @ResponseBody
    @RequestMapping("/getActionSize")
    public long getActionSize(HttpServletRequest request){
        String openId=(String)request.getAttribute("userId");
        return userService.getActionSize(openId);
    }
    @ResponseBody
    @RequestMapping("/getActionList")
    public Map<String,Object> getActionList(HttpServletRequest request,int page){
        String openId=(String)request.getAttribute("userId");
        return articleService.getActionList(openId,page);
    }
    @ResponseBody
    @RequestMapping("/getWorkList")
    public Map<String,Object> getWorkList(HttpServletRequest request,int page){
        String openId=(String)request.getAttribute("userId");
        return articleService.getWorkList(openId,page);
    }
}
