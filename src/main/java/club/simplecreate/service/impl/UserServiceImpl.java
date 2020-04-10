package club.simplecreate.service.impl;

import club.simplecreate.cache.*;
import club.simplecreate.dao.UserMapper;
import club.simplecreate.pojo.Article;
import club.simplecreate.pojo.User;
import club.simplecreate.service.UserService;
import club.simplecreate.utils.DateUtil;
import club.simplecreate.utils.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        String token;
        //首先根据code解析openid
        String openid=WxUtils.resolveCode(user.getCode());
        user.setOpenid(openid);
        //设置这次登录时间
        //然后利用openid判断该用户在缓存中是否存在
        User userResultFromCache=userCache.selectUserByOpenid(openid);
        if(userResultFromCache==null) {
            //不在则判断是否在数据库中,不在则加入并建立缓存
            User userResultFromDb=userMapper.selectUserByOpenid(openid);
            if(userResultFromDb==null) {
                user.setOpenid(openid);
                if(userMapper.insertUser(user)==1){
                    //新用户加入成功，建立缓存
                    userCache.insertUser(user);
                    //设置当前时间为其刷新时间
                    userCache.setNewLastReloadTime(openid);
                }else{
                    //加入失败
                    return null;
                }
            }else{
                //重建缓存
                userCache.insertUser(userResultFromDb);
            }
        }
//        else {
//            //如果用户信息发生变化
//            if(!userResultFromCache.equals(user)){
//                //更新缓存
//                userCache.insertUser(user);
//                //更新数据库
//                userMapper.updateUser(user);
//            }
//        }//生成token,加入redis key为token,value为用户openid,一小时后过期
        token=UUID.randomUUID().toString();
        tokenCache.setToken(token,openid);
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
            //延迟存活时间
            tokenCache.setTokenTTL(token);
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
            userCache.insertUser(userResultFromDb);
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
        return likeCache.isLike(openId,articleId);
    }

    @Override
    public boolean isFavorite(String openId, String articleId) {
        return favoriteCache.isFavorite(openId,articleId);
    }

    @Override
    public boolean isFollow(String openId, String userId) {
        return followCache.isFollow(openId,userId);
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

    @Override
    public Map<String, Object> getFollowList(String openId, int page) {
        Map<String, Object> res=new HashMap<>(2);
        res.put("totalNums",userCache.getFollowListSize(openId));
        Set<Object> userIds=userCache.getFollowList(openId,page);
        List<User> users=getUsers(userIds);
        res.put("rows",users);
        return res;
    }

    @Override
    public long getActionSize(String openId) {
        //先删除之前关注的人作品列表合集
        userCache.deleteActionSet(openId);
        //再重新合并关注的人作品列表
        userCache.unionActionSet(openId);
        //取出该上一次刷新时间
        long start=userCache.getLastReloadTime(openId);
        //该时间内到现在有多少作品即为新动态
        return  userCache.getActionSize(openId,start,System.currentTimeMillis());
    }



    private List<User> getUsers(Collection<Object> userIds){
        List<User> users=new ArrayList<>();
        for(Object userId:userIds) {
            users.add(selectUserById((String) userId));
        }
        return users;
    }


}
