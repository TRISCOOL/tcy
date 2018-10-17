package tcy.common.model;

import java.math.BigDecimal;
import java.util.Date;

public class IntegralRecord {
    private Long id;

    private Long userId;

    private Byte operationType;

    private BigDecimal moneyValue;

    private BigDecimal integralValue;

    private Long productId;

    private Long sharedId;

    private Date createTime;

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

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getMoneyValue() {
        return moneyValue;
    }

    public void setMoneyValue(BigDecimal moneyValue) {
        this.moneyValue = moneyValue;
    }

    public BigDecimal getIntegralValue() {
        return integralValue;
    }

    public void setIntegralValue(BigDecimal integralValue) {
        this.integralValue = integralValue;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSharedId() {
        return sharedId;
    }

    public void setSharedId(Long sharedId) {
        this.sharedId = sharedId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}