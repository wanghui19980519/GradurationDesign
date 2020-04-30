package club.simplecreate.cache;

import club.simplecreate.pojo.Article;
import club.simplecreate.utils.Ansj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ArticleCache {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Async
    public void insertArticle(Article article){
        //文章总数加一
        redisTemplate.opsForValue().increment("ARTICLE_COUNT");
        //将该文章加入topN列表Zset
        redisTemplate.opsForZSet().add("TOPN",article.getArticleId(),0);
        //将该文章加入newN列表List
        redisTemplate.opsForList().leftPush("NEWN",article.getArticleId());
        //将该文章加入该类别列表Zset
        redisTemplate.opsForZSet().add("TYPE_TOPN:"+article.getTypeId(),article.getArticleId(),0);
        //为该文章建立缓存
        //切割出第一张图片
        if(article.getContainImgpath()!=null) {
            String containImg = article.getContainImgpath().split(",")[0];
            article.setContainImgpath(containImg);
        }
        redisTemplate.opsForValue().set("ARTICLE:"+article.getArticleId(),article);
        //将该文章加入该作者的作品列表中Zset，score为时间
        redisTemplate.opsForZSet().add("USER_ARTICLES:"+article.getUserId(),article.getArticleId(),System.currentTimeMillis());
        //建立访问量缓存
        redisTemplate.opsForValue().set("VISIT_NUMS:"+article.getArticleId(),0);
    }

    public void deleteArticle(String articleId,String userId) {
        //删除该文章缓存
        redisTemplate.opsForValue().decrement("ARTICLE_COUNT");
        redisTemplate.delete("ARTICLE:"+articleId);
        //删除topN中该id
        redisTemplate.opsForZSet().remove("TOPN",articleId);
        //删除为该文章建立的倒排索引
        Set<Object> keywordSet=redisTemplate.opsForZSet().range("ARTICLE_KEYWORDS:"+articleId,0,-1);
        for(Object keyword:keywordSet){
            redisTemplate.opsForZSet().remove("KEYWORD:"+keyword,articleId);
        }
        //删除该文章的关键字字段
        redisTemplate.delete("ARTICLE_KEYWORDS:"+articleId);
        //删除该文章的点赞列表
        redisTemplate.delete("ARTICLE_LIKES:"+articleId);
        //删除该文章的评论列表
        redisTemplate.delete("ARTICLE_COMMENTS:"+articleId);
        //待解决：有人关注了这篇文章，存在问题删除不了
    }
    @Async
    public void keywordSearch(Article article) {
        //为该文章标题，标签,摘要进行分词，建立倒排索引Zset  关键字--->文章id score词频，
        //同时也将文章关键字存入文章缓存用于计算文章相似度
        Map<String,Double> word=Ansj.split(article.getTitle(),article.getTag(),article.getSummary());
        for(Map.Entry<String, Double> entry: word.entrySet())
        {
            //进行逆文档频率计算

            //1.包含该关键字的文档数
            long nums=redisTemplate.opsForZSet().zCard("KEYWORD:"+entry.getKey());
            //2.文档总数
            Integer articleCount= (Integer) redisTemplate.opsForValue().get("ARTICLE_COUNT");
            
            double idf= Math.log10(articleCount/(nums+1));
            //加入该文章的关键字列表
            redisTemplate.opsForZSet().add("ARTICLE_KEYWORDS:"+article.getArticleId(),entry.getKey(),entry.getValue()*idf);
            //建立倒排索引
            redisTemplate.opsForZSet().add("KEYWORD:"+entry.getKey(),article.getArticleId(),entry.getValue()*idf);
        }
    }
    @Async
    public void increaseVisits(String articleId) {
        //添加访问量
        redisTemplate.opsForValue().increment("VISIT_NUMS:"+articleId);
    }
    public Article selectArticleById(String articleId){
        //从缓存中获取该文章
        return (Article)redisTemplate.opsForValue().get("ARTICLE:"+articleId);
    }
    public int getVisitNums(String articleId){
        if(redisTemplate.hasKey("VISIT_NUMS:"+articleId)) {
            return (int) redisTemplate.opsForValue().get("VISIT_NUMS:" + articleId);
        }else{
            redisTemplate.opsForValue().set("VISIT_NUMS:" + articleId,0);
            return 0;
        }
    }
    @Async
    public void increaseScore(String articleId) {
        redisTemplate.opsForZSet().incrementScore("TOPN",articleId,1);
    }

    public long getTopNSize() {
        return redisTemplate.opsForZSet().size("TOPN");
    }

    public Set<Object> getTopNArticleList(int page) {
        return redisTemplate.opsForZSet().reverseRange("TOPN", (page-1)*6,page*6-1);
    }

    public long getNewNSize() {
        return redisTemplate.opsForList().size("NEWN");
    }

    public List<Object> getNewNArticleList(int page) {
        return redisTemplate.opsForList().range("NEWN",(page-1)*6,page*6-1);
    }

    public Set<Object> getSpecialRecommendList(String openId, int page) {
        return redisTemplate.opsForZSet().reverseRange("SPECIAL_RECOMMEND:"+openId,(page-1)*6,page*6-1);
    }
    public long getCommentNums(String articleId){
        return redisTemplate.opsForList().size("ARTICLE_COMMENTS:"+articleId);
    }
    public long getLikeNums(String articleId){
        return redisTemplate.opsForSet().size("ARTICLE_LIKES:"+articleId);
    }
    public long getSpecialRecommendListSize(String openId) {
        return redisTemplate.opsForZSet().size("SPECIAL_RECOMMEND:"+openId);
    }

    public Set<Object> getDefaultRecommendList(int page) {
        if(!redisTemplate.hasKey("DEFAULT_RECOMMEND_LIST")){
            Set<Object> topKeyWords= redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
            for(Object keyword: topKeyWords){
                redisTemplate.opsForZSet().unionAndStore("DEFAULT_RECOMMEND_LIST","KEYWORD:"+keyword,"DEFAULT_RECOMMEND_LIST");
            }
        }
        return redisTemplate.opsForZSet().reverseRange("DEFAULT_RECOMMEND_LIST",(page-1)*6,page*6-1);
    }

    public long getDefaultRecommendListSize() {
        return redisTemplate.opsForZSet().size("DEFAULT_RECOMMEND_LIST");
    }
}
