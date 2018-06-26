package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ProductType;

import java.util.List;

public interface ProductTypeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductType record);

    int insertSelective(ProductType record);

    ProductType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductType record);

    int updateByPrimaryKey(ProductType record);

    List<ProductType> selectProductTypeByLevel(@Param("parentId")Long parentId,
                                               @Param("level")Integer level);
}