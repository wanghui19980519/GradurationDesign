package club.simplecreate.message;

import club.simplecreate.pojo.User;

public class LikeMessage {
    private String avatarPath;
    private String userId;
    private String title;
    private String articleId;
    //某某点赞了你的某某文章
    public LikeMessage(User user, String articleId, String title) {
        this.articleId=articleId;
        this.userId=user.getAvatarPath();
        this.avatarPath=user.getAvatarPath();
        this.title=title;
    }


    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
