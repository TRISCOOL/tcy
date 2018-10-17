package tcy.api.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.Product;
import tcy.common.model.ShoppingCart;
import tcy.common.model.User;
import tcy.common.service.ProductService;
import tcy.common.service.ShoppringCartService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shopping")
public class ShoppingCartController extends BaseController{

    @Autowired
    private ShoppringCartService shoppringCartService;

    @Autowired
    private ProductService productService;

    /**
     * 添加商品到购物车
     * @param shoppingCart
     * @param request
     * @return
     */
    @PostMapping("/add/v1.1")
    public ResponseVo addShoppingCart(@ModelAttribute ShoppingCart shoppingCart,
                                      HttpServletRequest request){

        User user = curUser(request);
        if (user == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        if (user.getId() == null)
            return ResponseVo.error(ResponseCode.AUTH_FAILED);

        if (shoppingCart.getProductId() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        if (shoppingCart.getNum() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        Double cost = computeCost(shoppingCart.getProductId(),shoppingCart.getNum());
        if (cost == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        shoppingCart.setCost(new BigDecimal(cost));
        shoppingCart.setUserId(user.getId());

        boolean result = shoppringCartService.addProductToShoppingCart(shoppingCart);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 购物车列表
     * @return
     */
    @GetMapping("/list/v1.1")
    public ResponseVo listShoppingCart(HttpServletRequest request){
        User user = curUser(request);
        if (user == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        List<ShoppingCart> shoppingCarts = shoppringCartService.listShoppingCartForUser(user.getId());
        return ResponseVo.ok(shoppingCarts);
    }

    /**
     * 更新购物车商品数量
     * @param scId
     * @param num
     * @return
     */
    @PostMapping("/update/v1.1")
    public ResponseVo updateNum(@RequestParam("scId")Long scId,@RequestParam("num") Integer num){

        if (scId == null || num == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(scId);

        Double cost = computeCost(scId,num);
        shoppingCart.setNum(num);
        shoppingCart.setCost(new BigDecimal(cost));

        boolean result = shoppringCartService.updateShoppingCart(shoppingCart);
        if (result){
            return ResponseVo.ok(shoppingCart);
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);

    }

    /**
     * 删除某一个购物车商品
     * @param scId
     * @return
     */
    @PostMapping("/empty/v1.1")
    public ResponseVo delete(@RequestParam("scId")Long scId){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(3);
        shoppingCart.setId(scId);

        boolean result = shoppringCartService.updateShoppingCart(shoppingCart);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 清空购物车
     * @param request
     * @return
     */
    @PostMapping("/empty_all/v1.1")
    public ResponseVo deleteAll(HttpServletRequest request){
        User user = curUser(request);
        if (user == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        boolean result = shoppringCartService.emptyAllShoppingCartForUser(user.getId());
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    private Double computeCost(Long ccId,Integer num){
        Product product = productService.getProductByClothingConfig(ccId);
        if (product == null){
            return null;
        }

        Double cost = product.getRetailPrice().doubleValue();
        return cost*num;
    }
}
