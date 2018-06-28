package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ClothingConfig;

public interface ClothingConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ClothingConfig record);

    int insertSelective(ClothingConfig record);

    ClothingConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClothingConfig record);

    int updateByPrimaryKey(ClothingConfig record);

    ClothingConfig selectOneByAllId(@Param("productId")Long productId,
                                    @Param("colorId")Long colorId,
                                    @Param("sizeId")Long sizeId);
}