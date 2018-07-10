package tcy.common.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    private Long id;

    private Long userId;

    private String orderNumber;

    private BigDecimal actuallyPaid;

    private BigDecimal shouldPayAmount;

    private Integer status;

    private Date createTime;

    private String tareOrderNum;

    private Integer payType;

    private Date payTime;

    private Long addressId;

    private String blankNum;

    private String waybillNumber;

    private Integer delete;

    private List<ProductVo> productVoList;

    public List<ProductVo> getProductVoList() {
        return productVoList;
    }

    public void setProductVoList(List<ProductVo> productVoList) {
        this.productVoList = productVoList;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    public BigDecimal getActuallyPaid() {
        return actuallyPaid;
    }

    public void setActuallyPaid(BigDecimal actuallyPaid) {
        this.actuallyPaid = actuallyPaid;
    }

    public BigDecimal getShouldPayAmount() {
        return shouldPayAmount;
    }

    public void setShouldPayAmount(BigDecimal shouldPayAmount) {
        this.shouldPayAmount = shouldPayAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTareOrderNum() {
        return tareOrderNum;
    }

    public void setTareOrderNum(String tareOrderNum) {
        this.tareOrderNum = tareOrderNum == null ? null : tareOrderNum.trim();
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getBlankNum() {
        return blankNum;
    }

    public void setBlankNum(String blankNum) {
        this.blankNum = blankNum == null ? null : blankNum.trim();
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber == null ? null : waybillNumber.trim();
    }
}