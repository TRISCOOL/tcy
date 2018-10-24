package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.IntegralConfig;

public interface IntegralConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IntegralConfig record);

    int insertSelective(IntegralConfig record);

    IntegralConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IntegralConfig record);

    int updateByPrimaryKey(IntegralConfig record);

    IntegralConfig selectIntegralByProductId(@Param("productId")Long productId);
}