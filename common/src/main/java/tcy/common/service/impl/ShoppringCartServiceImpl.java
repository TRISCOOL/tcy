package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.ShoppingCartMapper;
import tcy.common.model.ShoppingCart;
import tcy.common.service.ShoppringCartService;

import java.util.List;

@Service
public class ShoppringCartServiceImpl implements ShoppringCartService{

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public boolean addProductToShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setStatus(0);
        int result = shoppingCartMapper.insertSelective(shoppingCart);
        if (result != 0){
            return true;
        }
        return false;
    }

    @Override
    public List<ShoppingCart> listShoppingCartForUser(Long userId) {
        return shoppingCartMapper.listShoppingCartByUserId(userId);
    }

    @Override
    public boolean deleteOneShoppingCartWithId(Long shoppingCartId) {
        return false;
    }

    @Override
    public boolean updateShoppingCart(ShoppingCart shoppingCart) {
        int result = shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart);
        if (result != 0){
            return true;
        }

        return false;
    }

    @Override
    public boolean emptyAllShoppingCartForUser(Long userId) {
        int result = shoppingCartMapper.deleteAllByUserId(userId);
        if (result != 0){
            return true;
        }

        return false;
    }
}
