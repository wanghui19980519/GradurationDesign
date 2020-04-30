package club.simplecreate.controller;

import club.simplecreate.pojo.Article;
import club.simplecreate.pojo.Comment;
import club.simplecreate.pojo.Reply;
import club.simplecreate.pojo.User;
import club.simplecreate.service.ArticleService;
import club.simplecreate.service.CommentService;
import club.simplecreate.service.ReplyService;
import club.simplecreate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    ReplyService replyService;


    @ResponseBody
    @RequestMapping("/getTopNList")
    public Map<String,Object> getTopList(int page){
        return articleService.getTopNList(page);
    }
    @ResponseBody
    @RequestMapping("/getNewNList")
    public Map<String,Object> getNewList(int page){
        return articleService.getNewNList(page);
    }
    @ResponseBody
    @RequestMapping("/getSpecialRecommendList")
    public Map<String,Object> getSpecialRecommendList(HttpServletRequest request,int page){
        String openId=(String)request.getAttribute("userId");
        return articleService.getSpecialRecommendList(openId,page);
    }
    @ResponseBody
    @RequestMapping("/setSpecialRecommendList")
    public void setSpecialRecommendList(HttpServletRequest request){
        String openId=(String)request.getAttribute("userId");
        articleService.setSpecialRecommendList(openId);
    }
    @ResponseBody
    @RequestMapping("/getDefaultRecommendList")
    public Map<String,Object> getDefaultRecommendList(int page){
        return articleService.getDefaultRecommendList(page);
    }
    @ResponseBody
    @RequestMapping("/insertArticle")
    public Map<String,String> insertArticle(HttpServletRequest request,Article article){
        String openId=(String)request.getAttribute("userId");
        article.setUserId(openId);
        article.setPublishTime(new Date());
        //设置id
        article.setArticleId(UUID.randomUUID().toString());
        Map<String,String> res=new HashMap<>(2);
        if(articleService.insertArticle(article)){
            //文章加入成功
            res.put("result","success");
            return res;
        }
        else{
            res.put("result","fail");
            return res;
        }
    }
    @ResponseBody
    @RequestMapping("/selectArticleById")
    public Map<String,Object> selectArticleById(HttpServletRequest request,String articleId){
        //查询文章
        Map<String,Object> res=new HashMap<>();
        //任何人都可以访问文章，所以不需要拦截
        Article article=articleService.selectArticleById(articleId);
        //文章浏览数加一
        articleService.increaseVisitNums(articleId);
        res.put("article",article);
        return res;
    }
    @ResponseBody
    @RequestMapping("/selectArticleByIdForLoginUser")
    public void selectArticleByIdForLoginUser(HttpServletRequest request,String articleId){
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        //将该文章的关键字列表加入用户的兴趣列表
        userService.insertArticleKeyWord(openId,articleId);
        //将该文章加入该用户的浏览历史
        userService.insertHistory(openId,articleId);
        //topN热度加1
        articleService.increaseScore(articleId);
    }
    @ResponseBody
    @RequestMapping("/isLike")
    public boolean isLike(HttpServletRequest request,String articleId){
        //判断该文章是否点赞过
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        return userService.isLike(openId,articleId);
    }
    @ResponseBody
    @RequestMapping("/isFavorite")
    public boolean isFavorite(HttpServletRequest request,String articleId){
        //判断该文章是否收藏过
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        return userService.isFavorite(openId,articleId);
    }
    @ResponseBody
    @RequestMapping("/isFollow")
    public boolean isFollow(HttpServletRequest request,String authorId){
        //判断该用户是否关注过
         //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        return userService.isFollow(openId,authorId);
    }
    @ResponseBody
    @RequestMapping("/getCommentList")
    public List<Comment> getCommentList(String articleId){
        //获取文章评论列表，获取每个评论的回复数
        return commentService.getCommentList(articleId);
    }
    @ResponseBody
    @RequestMapping("/insertComment")
    public boolean insertComment(HttpServletRequest request,Comment comment){
        //发表评论
        String openId=(String)request.getAttribute("userId");
        //取出评论人信息
        User sender=userService.selectUserById(openId);
        comment.setUserId(openId);
        comment.setSenderAvatarPath(sender.getAvatarPath());
        comment.setSenderNickname(sender.getNickname());
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setPublishTime(new Date());
        return commentService.insertComment(comment);
    }
    @ResponseBody
    @RequestMapping("/insertReply")
    public boolean insertReply(HttpServletRequest request, Reply reply){
        //发表回复
        String openId=(String)request.getAttribute("userId");
        //取出回复人信息
        User sender=userService.selectUserById(openId);
        reply.setReplyId(UUID.randomUUID().toString());
        reply.setPublishTime(new Date());
        reply.setSenderId(openId);
        reply.setSenderAvatarPath(sender.getAvatarPath());
        reply.setSenderNickname(sender.getNickname());
        return replyService.insertReply(reply);
    }
    @ResponseBody
    @RequestMapping("/follow")
    public boolean follow(HttpServletRequest request,String authorId){
        //关注
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        User user=userService.selectUserById(openId);
        return userService.follow(user,authorId);
    }
    @ResponseBody
    @RequestMapping("/like")
    public void like(HttpServletRequest request,String authorId,String articleId,String title){
        //点赞
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        User user=userService.selectUserById(openId);
        userService.like(user,authorId,articleId,title);
    }
    @ResponseBody
    @RequestMapping("/favorite")
    public boolean favorite(HttpServletRequest request,String articleId){
        //收藏
        //这是需要拦截的操作
        String openId=(String)request.getAttribute("userId");
        return userService.favorite(openId,articleId);
    }


}
