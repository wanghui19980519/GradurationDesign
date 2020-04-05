package club.simplecreate.message;

import club.simplecreate.pojo.User;

public class FollowMessage {
    private String avatartPath;

    private String openId;

    private String nickname;
    public FollowMessage(User user) {
        this.avatartPath=user.getAvatarPath();
        this.nickname=user.getNickname();
        this.openId=user.getOpenid();
    }

    public String getAvatartPath() {
        return avatartPath;
    }

    public void setAvatartPath(String avatartPath) {
        this.avatartPath = avatartPath;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
