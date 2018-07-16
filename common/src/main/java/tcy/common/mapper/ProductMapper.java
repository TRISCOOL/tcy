package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> listAllProduct(@Param("offset")Integer offset,@Param("length")Integer length);

    List<Product> listProductByType(@Param("offset")Integer offset,
                                    @Param("length")Integer length,
                                    @Param("productTypeId")Long productTypeId);

    List<Product> listProductBySearch(@Param("offset")Integer offset,
                                      @Param("length")Integer length,
                                      @Param("found")String found);

    List<Product> selectProductForTag(@Param("tag")Integer tag);

    Product selectProductByClothingConfig(@Param("ccId")Long ccId);

    List<Product> selectProductForEvaluated(@Param("userId")Long userId,
                                            @Param("isAppraise")Integer isAppraise,
                                            @Param("offset")Integer offset,
                                            @Param("length")Integer length);
}