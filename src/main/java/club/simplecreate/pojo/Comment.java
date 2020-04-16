package club.simplecreate.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Comment {


    private String commentId;

    private String commentContent;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date publishTime;

    private String articleId;

    private String userId;

    private String senderNickname;

    private long replyNums;

    private String receiverId;

    private String title;

    private String senderAvatarPath;

    public String getSenderAvatarPath() {
        return senderAvatarPath;
    }

    public long getReplyNums() {
        return replyNums;
    }

    public void setReplyNums(long replyNums) {
        this.replyNums = replyNums;
    }

    public void setSenderAvatarPath(String senderAvatarPath) {
        this.senderAvatarPath = senderAvatarPath;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}