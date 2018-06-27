package tcy.common.service;

import tcy.common.model.Product;
import tcy.common.model.ProductType;

import java.util.List;

public interface ProductService {

    List<Product> listAllProduct(Integer offset,Integer length);

    List<Product> listProductByType(Integer offset,Integer length,Long productTypeId);

    List<ProductType> listProductType(Long parentId,Integer level);

    List<Product> listProductBySearch(Integer offset,Integer length,String found);

    List<Product> listProductWithTag(Integer tag);
}
