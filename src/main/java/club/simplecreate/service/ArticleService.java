package club.simplecreate.service;

import club.simplecreate.pojo.Article;

public interface ArticleService {
    boolean insertArticle(Article article);

    Article selectArticleById(String articleId);

    void increaseVisitNums(String articleId);

    void increaseScore(String articleId);
}
