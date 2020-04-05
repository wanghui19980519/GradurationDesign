package club.simplecreate.message;

import club.simplecreate.pojo.Comment;

import java.util.Date;

public class CommentMessage {

    private String senderId;

    private String senderAvatarPath;

    private String title;

    private String articleId;

    private Date publishTime;


    public CommentMessage() {

    }
    public CommentMessage(Comment comment) {
        this.articleId=comment.getArticleId();
        this.senderId=comment.getUserId();
        this.senderAvatarPath=comment.getSenderAvatarPath();
        this.publishTime=comment.getPublishTime();
        this.title=comment.getTitle();
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
