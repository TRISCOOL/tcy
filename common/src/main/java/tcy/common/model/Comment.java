package tcy.common.model;

import java.util.Date;

public class Comment {
    private Long id;

    private Long userId;

    private Long productId;

    private Date createTime;

    private Long parentId;

    private Byte leavel;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Byte getLeavel() {
        return leavel;
    }

    public void setLeavel(Byte leavel) {
        this.leavel = leavel;
    }
}