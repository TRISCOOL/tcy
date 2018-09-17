package tcy.admin.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.admin.vo.ResponseVo;
import tcy.common.dto.ClothesConfigDto;
import tcy.common.dto.StockMangerDTO;
import tcy.common.exception.ResponseCode;
import tcy.common.model.ClothingConfig;
import tcy.common.model.Product;
import tcy.common.model.ProductType;
import tcy.common.service.ProductService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductManagerController {

    @Autowired
    private ProductService productService;

    /**
     * 所有颜色列表
     * @return
     */
    @GetMapping("/color/v1.1")
    public ResponseVo colorList(){
        return ResponseVo.ok(productService.listAllColor());
    }

    /**
     * 所有尺寸列表
     * @return
     */
    @GetMapping("/size/v1.1")
    public ResponseVo sizeList(){
        return ResponseVo.ok(productService.listAllSize());
    }

    @PostMapping("/add/v1.1")
    public ResponseVo addProduct(@RequestBody Product product){

        Integer sum = product.getStock();
        Integer everyOneSum = getSumNum(product.getConfigDtos());

        if (!sum.equals(everyOneSum)){
            return ResponseVo.error(ResponseCode.PRODUCT_NUM_NEED_MATCH);
        }

        product.setStorageTime(new Date());
        product.setSellNum(0);
        product.setSource(1);
        Long productId = productService.addProduct(product);
        return ResponseVo.ok(productId);
    }

    private Integer getSumNum(List<ClothesConfigDto> configDtos){

        Integer sum = 0;

        if (configDtos != null && configDtos.size() > 0){
            for (ClothesConfigDto configDto : configDtos){
                if (configDto.getNum() != null){
                    sum = sum + configDto.getNum();
                }
            }
            return sum;
        }else {
            return sum;
        }
    }

    /**
     * 服装类型列表
     * @param parentId
     * @return
     */
    @GetMapping("/type_list/v1.1")
    public ResponseVo listType(@RequestParam(value = "parentId",required = false)Long parentId){

        Integer level = null;

        if (parentId == null)
            level = 1;

        List<ProductType> productTypes = productService.listProductType(parentId,level);
        return ResponseVo.ok(productTypes);
    }

    /**
     * 是否上架
     * @param found
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list_by_shelf/v1.1")
    public ResponseVo listUnShelfProduct(@RequestParam(value = "found",required = false)String found,
                                         @RequestParam("isShelf")Integer isShelf,
                                         @RequestParam("page")Integer page,
                                         @RequestParam("size")Integer size){
        Integer offset = page - 1 < 0?0:page-1;

        List<Product> productList = productService.listProductByShelf(isShelf,found,1L,offset*size,size);
        return ResponseVo.ok(productList);
    }

    /**
     * 上下架商品（1-上架，0-下架）
     * @param productId
     * @return
     */
    @PostMapping("/shelf/v1.1")
    public ResponseVo shelfProduct(@RequestParam("productId")Long productId,@RequestParam("isShelf")Integer isShelf){
        if (productId == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        boolean result = productService.shelfProduct(isShelf,productId);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 商品各配置详情
     * @param productId
     * @return
     */
    @GetMapping("/config_list/v1.1")
    public ResponseVo listConfigSell(@RequestParam("productId")Long productId){
        List<ClothingConfig> configs = productService.configListByProduct(productId);
        return ResponseVo.ok(configs);
    }

    /**
     * 更新库存
     * @param stocks
     * @param productId
     * @return
     */
    @PostMapping("/update_stock/v1.1")
    public ResponseVo makeUpStockForProduct(@RequestBody List<StockMangerDTO> stocks,Long productId){
        if (stocks.size() <= 0)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        boolean result = productService.updateStock(stocks,productId);
        if (result)
            return ResponseVo.ok();

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }



}
