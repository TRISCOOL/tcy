package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ClothingConfig;

import java.util.List;

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

    List<ClothingConfig> configListByProduct(@Param("productId")Long productId);

    int updateStockById(@Param("stockNum")Integer stockNum,@Param("productId")Long productId);
}