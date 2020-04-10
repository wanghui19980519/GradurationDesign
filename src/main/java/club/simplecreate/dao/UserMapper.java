package club.simplecreate.dao;

import club.simplecreate.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     *
     * @param code
     * @return
     */
    User selectUserByOpenid(String code);


    int insertUser(User user);

    void updateUser(User user);
}