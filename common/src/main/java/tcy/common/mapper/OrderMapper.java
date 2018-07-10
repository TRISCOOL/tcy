package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectOrderByOrderNumberAndStatus(@Param("orderNumber")String orderNumber);

    List<Order> selectOrdersByStatusAndUser(@Param("status")Integer status,@Param("userId")Long userId);
}