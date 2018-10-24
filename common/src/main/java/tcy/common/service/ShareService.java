package tcy.common.service;

import tcy.common.model.ShareOperationRecord;
import tcy.common.model.ShareRecord;

import java.util.List;

public interface ShareService {

    /**
     * 记录一条分享，并返回分享id
     * @param shareRecord
     * @return
     */
    Long recordShare(ShareRecord shareRecord);

    /**
     * 用户的分享列表
     * @param userId
     * @return
     */
    List<ShareRecord> listShareForUser(Long userId);

    /**
     * 对某一分享的操作列表
     * @param shareId
     * @return
     */
    List<ShareOperationRecord> listOperationRecord(Long shareId);

    /**
     * 修改或添加某一条操作记录(在插入时，若有之前有对同一商品的分享操作，则将之前的置为忽略)
     * @param shareOperationRecord
     */
    void updateOrInsertOneOperationRecord(ShareOperationRecord shareOperationRecord);

    ShareOperationRecord getOne(Long shareId,Long userId);

    List<ShareOperationRecord> selectOperations(Long product,Long userId,Integer operationStatus);

    boolean updateShareOperationRecord(ShareOperationRecord shareOperationRecord);



}
