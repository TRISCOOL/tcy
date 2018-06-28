package tcy.api.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.controller.BaseController;
import tcy.api.vo.ProductTypeVo;
import tcy.api.vo.ProductVo;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.*;
import tcy.common.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController{

    @Autowired
    private ProductService productService;

    @GetMapping("/list/v1.1")
    public ResponseVo listAllProduct(@RequestParam("page")Integer page,
                                     @RequestParam("length")Integer length){

        Integer offset = page - 1 <= 0?0:page-1;
        List<Product> productList = productService.listAllProduct(offset*length,length);

        return ResponseVo.ok(getProductVoList(productList));
    }

    /**
     * 通过商品类型获得商品列表
     * @param page
     * @param length
     * @param productTypeId
     * @return
     */
    @GetMapping("/list_by_type/v1.1")
    public ResponseVo listProductByType(@RequestParam("page")Integer page,
                                        @RequestParam("length")Integer length,
                                        @RequestParam("productTypeId")Long productTypeId){

        if (productTypeId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        List<Product> productList = productService.listProductByType(page,length,productTypeId);
        return ResponseVo.ok(getProductVoList(productList));
    }

    /**
     * 商品类型列表
     * @param productTypeId
     * @param level
     * @return
     */
    @GetMapping("/type_list/v1.1")
    public ResponseVo productTypeList(@RequestParam(value = "productTypeId",required = false)Long productTypeId,
                                      @RequestParam("level")Integer level){

        if (productTypeId == null){
            return ResponseVo.ok(productService.listProductType(null,level));
        }else {

            List<ProductType> productTypes = productService.listProductType(productTypeId,null);
            List<ProductTypeVo> productTypeVoList = productTypes.stream().map(productType -> {
                ProductTypeVo productTypeVo = new ProductTypeVo();
                productTypeVo.setTypeName(productType.getName());
                productTypeVo.setTypeId(productType.getId());

                List<ProductType> productTypeList = productService.listProductType(productType.getId(),null);
                productTypeVo.setProductTypeList(productTypeList);

                return productTypeVo;
            }).collect(Collectors.toList());

            return ResponseVo.ok(productTypeVoList);

        }
    }

    /**
     * 搜索
     * @param found 搜索参数
     * @param page 当前页
     * @param length 页长度
     * @return
     */
    @GetMapping("/search/v1.1")
    public ResponseVo searchForProduct(@RequestParam("found")String found,@RequestParam("page")Integer page,
                                       @RequestParam("length")Integer length){

        Integer offset = page - 1 < 0?0:page-1;
        List<Product> productList = productService.listProductBySearch(offset*length,length,found);
        return ResponseVo.ok(getProductVoList(productList));
    }

    /**
     * 轮播图
     * @return
     */
    @GetMapping("/carousel/v1.1")
    public ResponseVo carousel(){
        List<Product> productList = productService.listProductWithTag(1);
        return ResponseVo.ok(getProductVoList(productList));
    }

    /**
     * 商品详情
     * @return
     */
    @GetMapping("/details/v1.1")
    public ResponseVo productDetail(@RequestParam("productId")Long productId){
        if (productId == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        Product product = productService.getProductDetails(productId);
        if (product == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        ProductVo vo = ProductVo.getVo(product);

        return ResponseVo.ok(vo);
    }

    /**
     * 获取指定商品的颜色
     * @param productId
     * @return
     */
    @GetMapping("/colors/v1.1")
    public ResponseVo listColorForProduct(@RequestParam("productId")Long productId){
        if (productId == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        List<Color> colors = productService.listColorWithProductId(productId);

        return ResponseVo.ok(colors);
    }

    /**
     * 获取指定商品的尺寸
     * @param productId
     * @return
     */
    @GetMapping("/sizes/v1.1")
    public ResponseVo listSizeForProduct(@RequestParam("productId")Long productId){
        if (productId == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        List<ClothingSize> clothingSizes = productService.listSizeWithProductId(productId);
        return ResponseVo.ok(clothingSizes);
    }

    @GetMapping("/check/v1.1")
    public ResponseVo checkProduct(@RequestParam("productId")Long productId,
                                   @RequestParam(value = "colorId",required = false)Long colorId,
                                   @RequestParam(value = "sizeId",required = false)Long sizeId){

        if (colorId == null && sizeId != null){
            List<Color> colorList = productService.checkColorByProductIdAndSizeId(productId,sizeId);
            return ResponseVo.ok(colorList);
        }else if (colorId != null && sizeId == null){
            List<ClothingSize> clothingSizes = productService.checkSizeByProductIdAndColorId(productId,colorId);
            return ResponseVo.ok(clothingSizes);
        }else if (colorId != null && sizeId != null){
            ClothingConfig clothingConfig = productService.selectOneByAllId(productId,sizeId,colorId);
            return ResponseVo.ok(clothingConfig);
        }

        return ResponseVo.ok();
    }


    private List<ProductVo> getProductVoList(List<Product> products){
        if (products == null)
            return null;

        if (products.size()<=0)
            return null;

        List<ProductVo> productVoList = products.stream().map(product -> {
            ProductVo vo = ProductVo.getVo(product);
            return vo;
        }).collect(Collectors.toList());

        return productVoList;
    }

}
