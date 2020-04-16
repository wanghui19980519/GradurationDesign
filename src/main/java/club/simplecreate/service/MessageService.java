package club.simplecreate.service;

import java.util.Map;

public interface MessageService {
    Map<String, Object> getFollowMessage(String openId);

    Map<String, Object> getCommentMessage(String openId);

    Map<String, Object> getLikeMessage(String openId);

    void commentMessageAllRead(String openId);

    void likeMessageAllRead(String openId);

    void followMessageAllRead(String openId);



}
