package club.simplecreate.service.impl;

import club.simplecreate.cache.ArticleCache;
import club.simplecreate.cache.UserCache;
import club.simplecreate.dao.ArticleMapper;
import club.simplecreate.pojo.Article;
import club.simplecreate.recommender.BaseOnContent;
import club.simplecreate.recommender.UserCF;
import club.simplecreate.service.ArticleService;
import club.simplecreate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    BaseOnContent baseOnContent;
    @Autowired
    UserCF userCF;
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

    @Override
    public Map<String, Object> getTopNList(int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(articleCache.getTopNSize());
        res.put("totalPages",pages);
        Set<Object> articleRows=articleCache.getTopNArticleList(page);
        List<Article> articles=getArticles(articleRows);
        res.put("rows",articles);
        return res;
    }

    @Override
    public Map<String, Object> getNewNList(int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(articleCache.getNewNSize());
        res.put("totalPages",pages);
        List<Object> articleIds=articleCache.getNewNArticleList(page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }
    @Override
    public Map<String, Object> getHistoryList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(userCache.getHistoryListSize(openId));
        res.put("totalPages",pages);
        Set<Object> articleIds=userCache.getHistoryList(openId,page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }
    @Override
    public Map<String, Object> getFavoriteList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(userCache.getFavoriteListSize(openId));
        res.put("totalPages",pages);
        Set<Object> articleIds=userCache.getFavoriteList(openId,page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }

    @Override
    public Map<String, Object> getActionList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(userCache.getActionListSize(openId));
        res.put("totalPages",pages);
        Set<Object> articleIds=userCache.getActionList(openId,page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }

    @Override
    public Map<String, Object> getWorkList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(userCache.getWorkListSize(openId));
        res.put("totalPages",pages);
        Set<Object> articleIds=userCache.getWorkList(openId,page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }

    @Override
    public Map<String, Object> getSpecialRecommendList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        long pages=getPages(articleCache.getSpecialRecommendListSize(openId));
        res.put("totalPages",pages);
        Set<Object> articleIds=articleCache.getSpecialRecommendList(openId,page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        return res;
    }

    @Override
    public Map<String, Object> getDefaultRecommendList(int page) {
        Map<String, Object> res=new HashMap<>(2);
        Set<Object> articleIds=articleCache.getDefaultRecommendList(page);
        List<Article> articles=getArticles(articleIds);
        res.put("rows",articles);
        long pages=getPages(articleCache.getDefaultRecommendListSize());
        res.put("totalPages",pages);
        return res;
    }

    @Override
    public void setSpecialRecommendList(String openId) {
        //生成推荐列表

        //基于用户的协同过滤推荐算法
        userCF.setSpecialRecommend(openId);
        //基于内容的推荐算法
        baseOnContent.setSpecialRecommend(openId);
    }

    private List<Article> getArticles(Collection<Object> articleRows){
        List<Article> articles=new ArrayList<>();
        for(Object articleId:articleRows) {
            Article temp=selectArticleById((String) articleId);
            if(temp!=null){
                temp.setContent(null);
                articles.add(temp);
            }//如果不在就删除一系列缓存中的该文章id
        }
        return articles;
    }
    private long getPages(long size){
        long pages=size/6;
        if(size%6!=0){
            pages+=1;
        }
        return  pages;
    }

}
