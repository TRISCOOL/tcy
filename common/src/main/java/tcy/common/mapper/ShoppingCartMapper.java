package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ShoppingCart;

import java.util.List;

public interface ShoppingCartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShoppingCart record);

    int insertSelective(ShoppingCart record);

    ShoppingCart selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShoppingCart record);

    int updateByPrimaryKey(ShoppingCart record);

    List<ShoppingCart> listShoppingCartByUserId(@Param("userId")Long userId);

    int deleteAllByUserId(@Param("userId")Long userId);
}