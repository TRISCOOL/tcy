package tcy.common.mapper;

public interface RelAddressUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RelAddressUser record);

    int insertSelective(RelAddressUser record);

    RelAddressUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RelAddressUser record);

    int updateByPrimaryKey(RelAddressUser record);
}