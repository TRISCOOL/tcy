package tcy.common.model;

import java.math.BigDecimal;

public class ShoppingCart {
    private Long id;

    private Long userId;

    private Long productId;

    private Integer num;

    private BigDecimal cost;

    private Byte status;

    private Long orderId;

    private Byte isAppraise;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Byte getIsAppraise() {
        return isAppraise;
    }

    public void setIsAppraise(Byte isAppraise) {
        this.isAppraise = isAppraise;
    }
}