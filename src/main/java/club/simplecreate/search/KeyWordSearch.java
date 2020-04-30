package club.simplecreate.search;

import club.simplecreate.cache.KeyWordCache;
import club.simplecreate.pojo.Article;
import club.simplecreate.service.ArticleService;
import club.simplecreate.utils.Ansj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Controller
public class KeyWordSearch {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private KeyWordCache keyWordCache;
    @Autowired
    ArticleService articleService;
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("/search")
    public Map<String ,Object> getResult(String keyword, int page){
        if(keyword==null){
            return null;
        }
        Map<String ,Object> res=new HashMap<>(3);
        Set<String> target=Ansj.split(keyword);
        if(!redisTemplate.hasKey("SEARCH_HISTORY:"+keyword)){
            //将搜索语句分词，然后将分解出对应关键字的倒排索引进行合并
            for(String word:target){
                redisTemplate.opsForZSet().unionAndStore("SEARCH_HISTORY:"+keyword,"KEYWORD:"+word,"SEARCH_HISTORY:"+keyword);
            }
        }
        //将key_word_topN的对应的关键字热度加10；
        keyWordCache.increaseKeyWordScore(target);
        //设置(延迟)过期时间1小时
        redisTemplate.expire("SEARCH_HISTORY:"+keyword,3,TimeUnit.HOURS);
        //返回所有结果
        Set<Object> rows=redisTemplate.opsForZSet().range("SEARCH_HISTORY:"+keyword,(page-1)*6,page*6-1);
        long totalNums=redisTemplate.opsForZSet().size("SEARCH_HISTORY:"+keyword);
        long totalPage=getPages(totalNums);
        List<Article> articles=getArticles(rows);
        res.put("rows",articles);
        res.put("totalNums",totalNums);
        res.put("totalPage",totalPage);
        return res;
    }
    @ResponseBody
    @RequestMapping("/getHotKeyWord")
    public Set<Object> getHotKeyWord(){
        return redisTemplate.opsForZSet().reverseRange("KEYWORD_TOP_N",0,9);
    }

    private List<Article> getArticles(Collection<Object> articleRows){
        List<Article> articles=new ArrayList<>();
        for(Object articleId:articleRows) {
            Article temp=articleService.selectArticleById((String) articleId);
            if(temp!=null){
                temp.setContent(null);
                articles.add(temp);
            }
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
