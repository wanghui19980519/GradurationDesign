package club.simplecreate.service;

import club.simplecreate.pojo.Article;

import java.util.Map;

public interface ArticleService {
    boolean insertArticle(Article article);

    Article selectArticleById(String articleId);

    void increaseVisitNums(String articleId);

    void increaseScore(String articleId);

    Map<String ,Object> getTopNList(int page);

    Map<String, Object> getNewNList(int page);
    
    Map<String, Object> getHistoryList(String openId, int page);

    Map<String, Object> getFavoriteList(String openId, int page);

    Map<String, Object> getActionList(String openId,int page);
}
