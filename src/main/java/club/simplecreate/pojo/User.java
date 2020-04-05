package club.simplecreate.pojo;

import java.util.Date;

public class User {

    private String openid;

    @Override
    public String toString() {
        return "User{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", avatarPath='" + avatarPath + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", code='" + code + '\'' +
                ", token=" + token +
                '}';
    }

    private String nickname;

    private Integer age;

    private Integer gender;

    private String avatarPath;

    private String province;

    private String city;

    private Date lastLoginTime;

    private String code;

    private Long token;

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath == null ? null : avatarPath.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}