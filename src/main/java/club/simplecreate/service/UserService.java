package club.simplecreate.service;

import club.simplecreate.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
   User wxLogin(User user);

    Map<String, Object> checkLogin(String token);

    User selectUserById(String openId);

    void insertArticleKeyWord(String openId, String articleId);

    void insertHistory(String openId, String articleId);

    boolean isLike(String openId, String articleId);

    boolean isFavorite(String openId, String articleId);

    boolean isFollow(String openId, String userId);

    boolean follow(User user, String authorId);

    boolean like(User user,String authorId, String articleId, String title);

    boolean favorite(String openId, String articleId);

    List<User> getFollowList(String openId);

    long getActionSize(String openId);


}
