package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Color;

import java.util.List;

public interface ColorMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Color record);

    int insertSelective(Color record);

    Color selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Color record);

    int updateByPrimaryKey(Color record);

    List<Color> selectColorsByProductId(@Param("productId")Long productId);

    List<Color> selectColorsByProductIdAndSizeId(@Param("productId")Long productId,@Param("sizeId")Long sizeId);
}