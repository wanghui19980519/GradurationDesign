package club.simplecreate.service;


import java.util.Map;

public interface MessageService {
    Map<String, Object> getFollowMessage(String openId);

    Map<String, Object> getCommentMessage(String openId);

    Map<String, Object> getLikeMessage(String openId);

    void commentMessageAllRead(String openId,int num);

    void likeMessageAllRead(String openId,int num);

    void followMessageAllRead(String openId,int num);

    void clearNewMessageNums(String openId);

    void readFollowMessageByIndex(String openId, int index);

    void readCommentMessageByIndex(String openId, int index);

    void readLikeMessageByIndex(String openId, int index);

    int  getNewMessageNum(String openId);
}
