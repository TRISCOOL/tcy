package tcy.api.vo;

import tcy.common.model.ProductType;

import java.util.List;

public class ProductTypeVo {
    private String typeName;
    private Long typeId;
    private List<ProductType> productTypeList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList) {
        this.productTypeList = productTypeList;
    }
}
