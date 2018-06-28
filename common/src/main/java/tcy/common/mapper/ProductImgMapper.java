package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ProductImg;

import java.util.List;

public interface ProductImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);

    List<ProductImg> listImagesForProductIdAndType(@Param("productId") Long productId,@Param("type")Integer type);
}