package club.simplecreate.service.impl;

import club.simplecreate.cache.*;
import club.simplecreate.dao.UserMapper;
import club.simplecreate.pojo.Favorite;
import club.simplecreate.pojo.Follow;
import club.simplecreate.pojo.User;
import club.simplecreate.service.UserService;
import club.simplecreate.utils.DateUtil;
import club.simplecreate.utils.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    TokenCache tokenCache;
    @Autowired
    UserCache userCache;
    @Autowired
    FollowCache followCache;
    @Autowired
    LikeCache likeCache;
    @Autowired
    FavoriteCache favoriteCache;
    @Override
    public User wxLogin(User user) {
        long token;
        //首先根据code解析openid
        System.out.println(user.getCode());
        String openid=WxUtils.resolveCode(user.getCode());
        System.out.println(openid);
        user.setOpenid(openid);
        //设置这次登录时间
        user.setLastLoginTime(new Date());
        //然后利用openid判断该用户在缓存中是否存在
        User userResultFromCache=userCache.selectUserByOpenid(openid);
        if(userResultFromCache==null)
        {
            //不在则判断是否在数据库中,不在则加入并建立缓存
            User userResultFromDb=userMapper.selectUserByOpenid(openid);
            if(userResultFromDb==null) {
                user.setOpenid(openid);
                if(userMapper.insertUser(user)==1){
                    //加入成功，建立缓存
                    userCache.insertUser(user);
                }else{
                    //加入失败
                    return null;
                }
            }else{
                //在则取出数据库中的最后登录时间
                user.setLastLoginTime(userResultFromDb.getLastLoginTime());
            }
            //不在建立缓存

        }else{
            //在则更新缓存
            userCache.insertUser(user);
            //取出上次登录时间返回
            user.setLastLoginTime(userResultFromCache.getLastLoginTime());
        }
        //生成token,加入redis key为token,value为用户openid,一小时后过期
        token=System.currentTimeMillis()- DateUtil.millis;

        tokenCache.setToken(token+"",openid);
        //返回token和用户更完整信息
        user.setToken(token);
        return user;
    }

    @Override
    public Map<String, Object> checkLogin(String token) {
        Map<String, Object> res=new HashMap<>();
        //利用该token从redis中判断是否存在
        String openid=tokenCache.getToken(token);
        //不存在则失败
        if(openid==null) {
            res.put("islogin", false);

        }
        else {
            res.put("islogin",true);
           res.put("userInfo",userCache.selectUserByOpenid(openid));
        }
        return res;
    }

    @Override
    public User selectUserById(String openId) {
        User userResultFromCache=userCache.selectUserByOpenid(openId);
        if(userResultFromCache==null) {
            User userResultFromDb = userMapper.selectUserByOpenid(openId);
            return userResultFromDb;
        }
        return userResultFromCache;
    }

    @Override
    public void insertArticleKeyWord(String openId, String articleId) {
        //合并兴趣列表和该文章的关键字列表
        userCache.insertArticleKeyWord(openId, articleId);
    }

    @Override
    public void insertHistory(String openId, String articleId) {
        //将该文章加入用户浏览历史列表
        userCache.insertHistory(openId,articleId);
    }

    @Override
    public boolean isLike(String openId, String articleId) {
        return userCache.isLike(openId,articleId);
    }

    @Override
    public boolean isFavorite(String openId, String articleId) {
        return userCache.isFavorite(openId,articleId);
    }

    @Override
    public boolean isFollow(String openId, String userId) {
        return userCache.isFollow(openId,userId);
    }

    @Override
    public boolean follow(User user, String authorId) {

        return followCache.follow(user,authorId);
    }

    @Override
    public boolean like(User user, String authorId,String articleId, String title) {

        return  likeCache.like(user,authorId,articleId,title);
    }

    @Override
    public boolean favorite(String openId, String articleId) {
        return favoriteCache.favorite(openId,articleId);
    }
}
