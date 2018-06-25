package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByOpenId(@Param("openId")String openId);
}