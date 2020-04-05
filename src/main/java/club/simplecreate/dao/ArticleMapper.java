package club.simplecreate.dao;


import club.simplecreate.pojo.Article;

public interface ArticleMapper {
    /**
     * 插入文章
     * @param article 文章信息
     * @return 插入成功还是失败
     */
    int insertArticle(Article article);

    Article selectArticleById(String articleId);
}