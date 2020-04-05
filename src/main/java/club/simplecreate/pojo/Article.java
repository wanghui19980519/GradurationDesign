package club.simplecreate.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Article {
    private String articleId;

    private String title;

    private String tag;

    private String summary;

    private String content;
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date publishTime;

    private String containImgpath;

    private  int visitNums;

    private  long commentNums;

    private  long likeNums;

    private String userId;

    private Integer typeId;

    private User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getContainImgpath() {
        return containImgpath;
    }

    public void setContainImgpath(String containImgpath) {
        this.containImgpath = containImgpath == null ? null : containImgpath.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public long getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(long commentNums) {
        this.commentNums = commentNums;
    }

    public long getLikeNums() {
        return likeNums;
    }

    public void setLikeNums(long likeNums) {
        this.likeNums = likeNums;
    }

    public int getVisitNums() {
        return visitNums;
    }

    public void setVisitNums(int visitNums) {
        this.visitNums = visitNums;
    }
}