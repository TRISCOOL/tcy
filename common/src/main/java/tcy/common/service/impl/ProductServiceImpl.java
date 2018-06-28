package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.*;
import tcy.common.model.*;
import tcy.common.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private ProductImgMapper productImgMapper;

    @Autowired
    private ColorMapper colorMapper;

    @Autowired
    private ClothingSizeMapper clothingSizeMapper;

    @Autowired
    private ClothingConfigMapper clothingConfigMapper;

    @Override
    public List<Product> listAllProduct(Integer offset, Integer length) {
        return productMapper.listAllProduct(offset,length);
    }

    @Override
    public List<Product> listProductByType(Integer offset, Integer length, Long productTypeId) {
        return productMapper.listProductByType(offset,length,productTypeId);
    }

    @Override
    public List<ProductType> listProductType(Long parentId, Integer level) {
        return productTypeMapper.selectProductTypeByLevel(parentId,level);
    }

    @Override
    public List<Product> listProductBySearch(Integer offset, Integer length, String found) {
        return productMapper.listProductBySearch(offset,length,found);
    }

    @Override
    public List<Product> listProductWithTag(Integer tag) {
        return productMapper.selectProductForTag(tag);
    }

    @Override
    public Product getProductDetails(Long productId) {

        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null)
            return null;


        List<ProductImg> productImgList = productImgMapper.listImagesForProductIdAndType(productId,2);
        if (productImgList != null && productImgList.size() > 0){
            List<String> imagesUrls = productImgList.stream().map(productImg -> {
                String url = productImg.getUrl();
                return url;
            }).collect(Collectors.toList());

            product.setDetailsImages(imagesUrls);
        }

        return product;
    }

    @Override
    public List<Color> listColorWithProductId(Long productId) {
        return colorMapper.selectColorsByProductId(productId);
    }

    @Override
    public List<ClothingSize> listSizeWithProductId(Long productId) {
        return clothingSizeMapper.selectSizeWithProductId(productId);
    }

    @Override
    public List<Color> checkColorByProductIdAndSizeId(Long productId, Long sizeId) {
        return colorMapper.selectColorsByProductIdAndSizeId(productId,sizeId);
    }

    @Override
    public List<ClothingSize> checkSizeByProductIdAndColorId(Long productId, Long colorId) {
        return clothingSizeMapper.selectSizeWithProductIdAndColorId(productId,colorId);
    }

    @Override
    public ClothingConfig selectOneByAllId(Long productId, Long sizeId, Long colorId) {
        return clothingConfigMapper.selectOneByAllId(productId,colorId,sizeId);
    }
}
