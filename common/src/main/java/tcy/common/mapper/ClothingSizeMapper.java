package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ClothingSize;

import java.util.List;

public interface ClothingSizeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ClothingSize record);

    int insertSelective(ClothingSize record);

    ClothingSize selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClothingSize record);

    int updateByPrimaryKey(ClothingSize record);

    List<ClothingSize> selectSizeWithProductId(@Param("productId")Long productId);

    List<ClothingSize> selectSizeWithProductIdAndColorId(@Param("productId")Long productId,
                                                         @Param("colorId")Long colorId);

    List<ClothingSize> listAllSize();
}