package tcy.common.mapper;

import tcy.common.model.ProductImg;

public interface ProductImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductImg record);

    int insertSelective(ProductImg record);

    ProductImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductImg record);

    int updateByPrimaryKey(ProductImg record);
}