package club.simplecreate.controller;

import club.simplecreate.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @ResponseBody
    @RequestMapping("/getFollowMessage")
    public Map<String, Object> getFollowMessage(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        return messageService.getFollowMessage(openId);
    }

    @ResponseBody
    @RequestMapping("/getLikeMessage")
    public Map<String, Object> getLikeMessage(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        return messageService.getLikeMessage(openId);
    }

    @ResponseBody
    @RequestMapping("/getCommentMessage")
    public Map<String, Object> getCommentMessage(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        return messageService.getCommentMessage(openId);
    }

    @ResponseBody
    @RequestMapping("/followMessageAllRead")
    public void followMessageAllRead(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        messageService.followMessageAllRead(openId);
    }

    @ResponseBody
    @RequestMapping("/commentMessageAllRead")
    public void commentMessageAllRead(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        messageService.commentMessageAllRead(openId);
    }

    @ResponseBody
    @RequestMapping("/likeMessageAllRead")
    public void likeMessageAllRead(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        messageService.likeMessageAllRead(openId);
    }
}
