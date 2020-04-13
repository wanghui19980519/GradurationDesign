package club.simplecreate.pojo;

import java.util.Date;

public class Message {

    private String senderId;

    private String senderAvatarPath;

    private String senderNickname;

    private String title;

    private String articleId;

    private Date publishTime;



    public Message() {

    }

    public Message(User user, String articleId, String title) {
        this.senderId=user.getOpenid();
        this.senderAvatarPath=user.getAvatarPath();
        this.senderNickname=user.getNickname();
        this.articleId=articleId;
        this.title=title;
        this.publishTime=new Date();
    }
    public Message(User user) {
        this.senderId=user.getOpenid();
        this.senderAvatarPath=user.getAvatarPath();
        this.senderNickname=user.getNickname();
        this.publishTime=new Date();
    }
    public Message(Comment comment) {
        this.articleId=comment.getArticleId();
        this.senderId=comment.getUserId();
        this.senderAvatarPath=comment.getSenderAvatarPath();
        this.publishTime=comment.getPublishTime();
        this.title=comment.getTitle();
        this.senderNickname=comment.getSenderNickname();
        this.publishTime=new Date();
    }
    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderAvatarPath() {
        return senderAvatarPath;
    }

    public void setSenderAvatarPath(String senderAvatarPath) {
        this.senderAvatarPath = senderAvatarPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

}
