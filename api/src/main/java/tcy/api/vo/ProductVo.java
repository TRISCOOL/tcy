package tcy.api.vo;

import tcy.common.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductVo {
    private Long productId;
    private String name;
    private String description;
    private Integer sellNum;
    private String brands;
    private BigDecimal retailPrice;
    private String url;

    private List<String> detailsImages;

    public static ProductVo getVo(Product product){
        if (product == null)
            return null;

        ProductVo vo = new ProductVo();
        vo.setBrands(product.getBrands());
        vo.setDescription(product.getDescription());
        vo.setName(product.getName());
        vo.setProductId(product.getId());
        vo.setRetailPrice(product.getRetailPrice());
        vo.setSellNum(product.getSellNum());
        vo.setUrl(product.getUrl());
        vo.setDetailsImages(product.getDetailsImages());

        return vo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getDetailsImages() {
        return detailsImages;
    }

    public void setDetailsImages(List<String> detailsImages) {
        this.detailsImages = detailsImages;
    }
}
