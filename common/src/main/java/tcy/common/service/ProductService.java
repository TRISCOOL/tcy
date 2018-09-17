package tcy.common.service;

import tcy.common.dto.StockMangerDTO;
import tcy.common.model.*;

import java.util.List;

public interface ProductService {

    List<Product> listAllProduct(Integer offset,Integer length);

    List<Product> listProductByType(Integer offset,Integer length,Long productTypeId);

    List<ProductType> listProductType(Long parentId,Integer level);

    List<Product> listProductBySearch(Integer offset,Integer length,String found);

    List<Product> listProductWithTag(Integer tag);

    Product getProductDetails(Long productId);

    List<Color> listColorWithProductId(Long productId);

    List<ClothingSize> listSizeWithProductId(Long productId);

    List<Color> checkColorByProductIdAndSizeId(Long productId,Long sizeId);

    List<ClothingSize> checkSizeByProductIdAndColorId(Long productId,Long colorId);

    ClothingConfig selectOneByAllId(Long productId,Long sizeId,Long colorId);

    Product getProductByClothingConfig(Long ccId);

    List<Color> listAllColor();

    List<ClothingSize> listAllSize();

    Long addProduct(Product product);

    List<Product> listProductByShelf(Integer isShelf,String found,Long shopId,Integer offset,Integer length);

    boolean shelfProduct(Integer isShelf,Long productId);

    List<ClothingConfig> configListByProduct(Long productId);

    boolean updateStock(List<StockMangerDTO> stockMangerDTOList,Long productId);
}
