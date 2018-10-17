package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcy.common.dto.ClothesConfigDto;
import tcy.common.dto.StockMangerDTO;
import tcy.common.exception.TcyException;
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

    @Override
    public Product getProductByClothingConfig(Long ccId) {
        return productMapper.selectProductByClothingConfig(ccId);
    }

    @Override
    public List<Color> listAllColor() {
        return colorMapper.listAllColor();
    }

    @Override
    public List<ClothingSize> listAllSize() {
        return clothingSizeMapper.listAllSize();
    }

    @Override
    @Transactional
    public Long addProduct(Product product) {

        productMapper.insertSelective(product);
        if (product.getId() == null)
            throw new TcyException("insert product failed cause by not get id");

        List<ClothesConfigDto> clothesConfigDtos = product.getConfigDtos();
        //添加具体的服装配置详情
        addClothConfig(product.getId(),clothesConfigDtos);
        //添加图片
        addImagesForProduct(product.getDetailsImages(),product.getUrl(),product.getId());

        return product.getId();
    }

    @Override
    public List<Product> listProductByShelf(Integer isShelf, String found, Long shopId,Integer offset,Integer length) {
        return productMapper.listProductForShelf(isShelf,found,shopId,offset,length);
    }

    @Override
    public boolean shelfProduct(Integer isShelf, Long productId) {
        int result = productMapper.shelfProductById(isShelf,productId);
        if (result != 0){
            return true;
        }

        return false;
    }

    @Override
    public List<ClothingConfig> configListByProduct(Long productId) {
        return clothingConfigMapper.configListByProduct(productId);
    }

    @Override
    @Transactional
    public boolean updateStock(List<StockMangerDTO> stockMangerDTOList,Long productId) {
        if (stockMangerDTOList.size() <= 0)
            return false;

        for (StockMangerDTO dto : stockMangerDTOList){
            clothingConfigMapper.updateStockById(dto.getStockNum(),dto.getConfigId());
        }

        List<ClothingConfig> configs = clothingConfigMapper.configListByProduct(productId);
        Integer everyStockNum = 0;
        for (ClothingConfig clothingConfig : configs){
            everyStockNum = everyStockNum + clothingConfig.getStockNum();
        }

        if (everyStockNum != 0){
            Product product = new Product();
            product.setStock(everyStockNum);
            product.setId(productId);
            productMapper.updateByPrimaryKeySelective(product);
        }

        return true;
    }

    @Override
    public List<Product> listProductByOrderId(Long orderId) {
        return productMapper.listProductByOrderId(orderId);
    }

    @Override
    public Product onlyGetProductById(Long productId) {
        return productMapper.onlyGetProductById(productId);
    }

    private void addImagesForProduct(List<String> detailsImages,String mainUrl,Long productId){
        if (mainUrl != null){
            ProductImg productImg = new ProductImg();
            productImg.setProductId(productId);
            productImg.setType(1+"");
            productImg.setUrl(mainUrl);
            productImgMapper.insertSelective(productImg);
        }

        if (detailsImages != null && detailsImages.size() > 0){
            for (String url : detailsImages){
                ProductImg productImg = new ProductImg();
                productImg.setUrl(url);
                productImg.setType(2+"");
                productImg.setProductId(productId);
                productImgMapper.insertSelective(productImg);
            }
        }
    }

    private void addClothConfig(Long productId,List<ClothesConfigDto> configDtos){
        if (configDtos == null)
            return;

        if (configDtos.size() <= 0)
            return;

        for (ClothesConfigDto configDto : configDtos){
            ClothingConfig clothingConfig = getConfig(configDto,productId);
            clothingConfigMapper.insertSelective(clothingConfig);
        }

    }

    private ClothingConfig getConfig(ClothesConfigDto dto,Long productId){
        ClothingConfig clothingConfig = new ClothingConfig();
        clothingConfig.setColorId(dto.getColorId());
        clothingConfig.setSellNum(0);
        clothingConfig.setColorValue(dto.getColorValue());
        clothingConfig.setProductId(productId);
        clothingConfig.setSizeValue(dto.getSizeValue());
        clothingConfig.setSizeId(dto.getSizeId());
        clothingConfig.setStockNum(dto.getNum());
        clothingConfig.setDescription(dto.getSizeValue()+"/"+dto.getColorValue());
        return clothingConfig;
    }
}
