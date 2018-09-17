package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.AdminUser;

public interface AdminUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    AdminUser selectByAccount(@Param("account")String account);
}