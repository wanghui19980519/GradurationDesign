package club.simplecreate.controller;

import club.simplecreate.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @ResponseBody
    @RequestMapping("/getReplyList")
    public List<Object> getReplyList(String commentId){
        return commentService.getReplyList(commentId);
    }
}
