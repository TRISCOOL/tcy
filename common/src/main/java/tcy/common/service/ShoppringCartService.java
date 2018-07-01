package tcy.common.service;

import tcy.common.model.ShoppingCart;

import java.util.List;

public interface ShoppringCartService {

    boolean addProductToShoppingCart(ShoppingCart shoppingCart);

    List<ShoppingCart> listShoppingCartForUser(Long userId);

    boolean deleteOneShoppingCartWithId(Long shoppingCartId);

    boolean updateShoppingCart(ShoppingCart shoppingCart);

    boolean emptyAllShoppingCartForUser(Long userId);

}
