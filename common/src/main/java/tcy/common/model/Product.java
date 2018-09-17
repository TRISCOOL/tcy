package tcy.common.model;

import tcy.common.dto.ClothesConfigDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product {
    private Long id;

    private String name;

    private Integer type;

    private String description;

    private Integer stock;

    private Integer sellNum;

    private BigDecimal cost;

    private BigDecimal retailPrice;

    private Integer source;

    private Integer tag;

    private Integer shelf;

    private Date shelfTime;

    private Date storageTime;

    private Long integralConfigId;

    private Integer subType;

    private String brands;

    private String url;

    private List<String> detailsImages;

    public List<ClothesConfigDto> getConfigDtos() {
        return configDtos;
    }

    public void setConfigDtos(List<ClothesConfigDto> configDtos) {
        this.configDtos = configDtos;
    }

    private List<ClothesConfigDto> configDtos;

    public List<String> getDetailsImages() {
        return detailsImages;
    }

    public void setDetailsImages(List<String> detailsImages) {
        this.detailsImages = detailsImages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getTag() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag = tag;
    }

    public Integer getShelf() {
        return shelf;
    }

    public void setShelf(Integer shelf) {
        this.shelf = shelf;
    }

    public Date getShelfTime() {
        return shelfTime;
    }

    public void setShelfTime(Date shelfTime) {
        this.shelfTime = shelfTime;
    }

    public Date getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(Date storageTime) {
        this.storageTime = storageTime;
    }

    public Long getIntegralConfigId() {
        return integralConfigId;
    }

    public void setIntegralConfigId(Long integralConfigId) {
        this.integralConfigId = integralConfigId;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }
}