package tcy.common.model;

import tcy.common.utils.DateTimeUtil;

import java.util.List;

public class OrderVo {
    private Long orderId;
    private String orderNum;
    private Double shouldPayAmount;
    private Double actuallyPaid;
    private String createTime;
    private Integer status;
    private String waybillNumber;
    private Address address;
    private List<ProductVo> productVos;

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getActuallyPaid() {
        return actuallyPaid;
    }

    public void setActuallyPaid(Double actuallyPaid) {
        this.actuallyPaid = actuallyPaid;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Double getShouldPayAmount() {
        return shouldPayAmount;
    }

    public void setShouldPayAmount(Double shouldPayAmount) {
        this.shouldPayAmount = shouldPayAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<ProductVo> getProductVos() {
        return productVos;
    }

    public void setProductVos(List<ProductVo> productVos) {
        this.productVos = productVos;
    }

    public static OrderVo getVo(Order order){
        OrderVo vo = new OrderVo();
        vo.setOrderId(order.getId());
        if (order.getCreateTime() != null){
            String time = DateTimeUtil.getYYYYMMDDHHMMSS(order.getCreateTime());
            vo.setCreateTime(time);
        }
        vo.setOrderNum(order.getOrderNumber());
        if (order.getShouldPayAmount() != null){
            vo.setShouldPayAmount(order.getShouldPayAmount().doubleValue());
        }
        if (order.getActuallyPaid() != null){
            vo.setActuallyPaid(order.getActuallyPaid().doubleValue());
        }
        vo.setStatus(order.getStatus());
        vo.setWaybillNumber(order.getWaybillNumber());

        return vo;
    }
}
