package tcy.common.mapper;

import tcy.common.model.UserRoleRel;

public interface UserRoleRelMapper {
    int deleteByPrimaryKey(UserRoleRel key);

    int insert(UserRoleRel record);

    int insertSelective(UserRoleRel record);
}