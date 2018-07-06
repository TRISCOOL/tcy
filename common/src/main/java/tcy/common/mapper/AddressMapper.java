package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Address;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Address> selectAddressByUser(@Param("userId")Long userId);

    Address selectDefaultAddress(@Param("userId")Long userId);

    int deleteAddressById(@Param("addressId")Long addressId);

    int upAddressTagByUser(@Param("tag")String tag,@Param("userId")Long userId);
}