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

    private Long shopId;

    private String province;
    private String city;
    private String detailAddress;
    private String contact;
    private String contactPhone;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

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