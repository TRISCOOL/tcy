package tcy.api.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.controller.BaseController;
import tcy.api.vo.ProductTypeVo;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.Product;
import tcy.common.model.ProductType;
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

        return ResponseVo.ok(productList);
    }

    /**
     *
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
        return ResponseVo.ok(productList);
    }

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
     *
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
        return ResponseVo.ok(productList);
    }


}
