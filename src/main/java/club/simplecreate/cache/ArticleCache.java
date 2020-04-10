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
        //文章发表时间距3月1日小时数
        //文章总数加一
        redisTemplate.opsForValue().increment("ARTICLE_COUNT");
        //将该文章加入topN列表Zset
        redisTemplate.opsForZSet().add("TOPN",article.getArticleId(),0);
        //将该文章加入newN列表List
        redisTemplate.opsForList().rightPush("NEWN",article.getArticleId());
        //将该文章加入该类别列表Zset
        redisTemplate.opsForZSet().add("TYPE_TOPN:"+article.getTypeId(),article.getArticleId(),0);
        //为该文章建立缓存
        redisTemplate.opsForValue().set("ARTICLE:"+article.getArticleId(),article);
        //将该文章加入该作者的作品列表中Zset，score为时间
        redisTemplate.opsForZSet().add("USER_ARTICLES:"+article.getUserId(),article.getArticleId(),System.currentTimeMillis());
        //建立访问量缓存
        redisTemplate.opsForValue().set("VISITNUMS:"+article.getArticleId(),0);

    }
    public void deleteArticle(Integer articleId,String userId,int typeId) {
        //删除该文章缓存
        redisTemplate.delete("ARTICLE:"+articleId);
        //删除作者作品列表下该文章
        redisTemplate.opsForZSet().remove("USER_ARTICLES:"+userId,articleId);
        //删除topN中该id
        redisTemplate.opsForZSet().remove("TOPN",articleId);
        //将该文章从该类别列表Zset中删除
        redisTemplate.opsForZSet().remove("TYPE_TOPN:"+typeId,articleId);
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
        redisTemplate.opsForValue().increment("VISITNUMS:"+articleId);
    }
    public Article selectArticleById(String articleId){
        //从缓存中获取该文章
        return (Article)redisTemplate.opsForValue().get("ARTICLE:"+articleId);
    }
    public int getVisitNums(String articleId){
        if(redisTemplate.hasKey("VISITNUMS:"+articleId)) {
            return (int) redisTemplate.opsForValue().get("VISITNUMS:" + articleId);
        }else{
            redisTemplate.opsForValue().set("VISITNUMS:" + articleId,0);
            return 0;
        }
    }
    public long getCommentNums(String articleId){
        return redisTemplate.opsForList().size("ARTICLE_COMMENTS:"+articleId);
    }
    public long getLikeNums(String articleId){
        return redisTemplate.opsForSet().size("ARTICLE_LIKES:"+articleId);
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
}
