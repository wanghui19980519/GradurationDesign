package club.simplecreate.service.impl;

import club.simplecreate.cache.ArticleCache;
import club.simplecreate.cache.UserCache;
import club.simplecreate.dao.ArticleMapper;
import club.simplecreate.pojo.Article;
import club.simplecreate.service.ArticleService;
import club.simplecreate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleCache articleCache;
    @Autowired
    UserCache userCache;
    @Autowired
    UserService userService;


    @Override
    public boolean insertArticle(Article article) {
        //然后加入数据库
        int res=articleMapper.insertArticle(article);
        //加入成功
        if(res==1){
            //然后加入缓存
            articleCache.insertArticle(article);
            //将该文章进行keyword拆分建立倒排索引
            articleCache.keywordSearch(article);
            return true;
        }
        //加入失败
        else{
            return false;
        }
    }

    @Override
    public Article selectArticleById(String articleId) {
        //先查看缓存中是否存在
        Article res=articleCache.selectArticleById(articleId);
        if(res==null){
            res=articleMapper.selectArticleById(articleId);
            if(res==null){
                return null;
            }else{
                articleCache.insertArticle(res);
            }
        }
        //获取作者信息
        res.setAuthor(userService.selectUserById(res.getUserId()));
        //获得其访问量，评论数，点赞数
        res.setVisitNums(articleCache.getVisitNums(articleId));
        res.setCommentNums(articleCache.getCommentNums(articleId));
        res.setLikeNums(articleCache.getLikeNums(articleId));

        return res;
    }

    @Override
    public void increaseVisitNums(String articleId) {
        //增加阅读量
        articleCache.increaseVisits(articleId);
    }

    @Override
    public void increaseScore(String articleId) {
         //增加文章分值
         articleCache.increaseScore(articleId);
    }

}
