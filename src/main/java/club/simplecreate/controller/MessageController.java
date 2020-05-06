package club.simplecreate.controller;

import club.simplecreate.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/message")
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
    public void followMessageAllRead(HttpServletRequest request,int num) {
        String openId = (String) request.getAttribute("userId");
        messageService.followMessageAllRead(openId,num);
    }

    @ResponseBody
    @RequestMapping("/commentMessageAllRead")
    public void commentMessageAllRead(HttpServletRequest request,int num) {
        String openId = (String) request.getAttribute("userId");
        messageService.commentMessageAllRead(openId,num);
    }

    @ResponseBody
    @RequestMapping("/likeMessageAllRead")
    public void likeMessageAllRead(HttpServletRequest request, int num) {
        String openId = (String) request.getAttribute("userId");
        messageService.likeMessageAllRead(openId,num);
    }
    @ResponseBody
    @RequestMapping("/clearNewMessageNums")
    public void clearNewMessageNums(HttpServletRequest request) {
        String openId = (String) request.getAttribute("userId");
        messageService.clearNewMessageNums(openId);
    }

    //这三个功能存在问题,通过message对象不能直接删除
    //通过index删除似乎高并发问题（就是在查看消息的同时有新的通知导致index错位）
    // 《---》
    // 解决办法，从后往前数，totalnums-index是不会变化的，就是该消息离最末尾消息的距离是不变的

    @ResponseBody
    @RequestMapping("/getNewMessageNum")
    public int getNewMessageNum(HttpServletRequest request){
        String openId = (String) request.getAttribute("userId");
        return messageService.getNewMessageNum(openId);
    }
    @ResponseBody
    @RequestMapping("/readFollowMessageByIndex")
    public void readFollowMessageByIndex(HttpServletRequest request,  int index,int totalNums) {
        String openId = (String) request.getAttribute("userId");
        messageService.readFollowMessageByIndex(openId,index-totalNums);
    }

    @ResponseBody
    @RequestMapping("/readCommentMessageByIndex")
    public void readCommentMessageByIndex(HttpServletRequest request, int index,int totalNums) {
        String openId = (String) request.getAttribute("userId");
        messageService.readCommentMessageByIndex(openId,index-totalNums);
    }

    @ResponseBody
    @RequestMapping("/readLikeMessageByIndex")
    public void readLikeMessageByIndex(HttpServletRequest request,int index,int totalNums) {
        String openId = (String) request.getAttribute("userId");
        messageService.readLikeMessageByIndex(openId,index-totalNums);
    }

}
