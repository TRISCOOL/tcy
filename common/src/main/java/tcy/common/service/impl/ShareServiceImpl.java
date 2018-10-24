package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcy.common.mapper.ShareOperationRecordMapper;
import tcy.common.mapper.ShareRecordMapper;
import tcy.common.model.ShareOperationRecord;
import tcy.common.model.ShareRecord;
import tcy.common.service.ShareService;

import java.util.List;

@Service
public class ShareServiceImpl implements ShareService{

    @Autowired
    private ShareRecordMapper shareRecordMapper;

    @Autowired
    private ShareOperationRecordMapper shareOperationRecordMapper;

    @Override
    public Long recordShare(ShareRecord shareRecord) {
        ShareRecord existShare = shareRecordMapper.selectShareRecord(shareRecord.getUserId(),shareRecord.getProductId());
        if (existShare != null)
            return existShare.getId();

        shareRecordMapper.insertSelective(shareRecord);
        return shareRecord.getId();
    }

    @Override
    public List<ShareRecord> listShareForUser(Long userId) {
        return shareRecordMapper.listShareRecord(userId);
    }

    @Override
    public List<ShareOperationRecord> listOperationRecord(Long shareId) {
        return shareOperationRecordMapper.listByShareId(shareId);
    }

    @Override
    @Transactional
    public void updateOrInsertOneOperationRecord(ShareOperationRecord shareOperationRecord) {

        if (!isContinue(shareOperationRecord))
            return;

        ShareOperationRecord exist = shareOperationRecordMapper.selectByShareIdAndUserId(shareOperationRecord.getShareId(),
                shareOperationRecord.getUserId());

        setOperationIgnore(exist.getProductId(),exist.getUserId());

        if (exist == null){
            shareOperationRecordMapper.insertSelective(shareOperationRecord);
        }else {
            shareOperationRecord.setId(exist.getId());
            shareOperationRecordMapper.updateByPrimaryKeySelective(shareOperationRecord);
        }

    }

    private boolean isContinue(ShareOperationRecord record){
        Long userId = record.getUserId();
        Long shareId = record.getShareId();

        ShareRecord shareRecord = shareRecordMapper.selectByPrimaryKey(shareId);
        if (shareRecord.getUserId() != null && shareRecord.getUserId().equals(userId))
            return false;

        return true;
    }

    /**
     * 将所有查看状态的置为ignore
     * @param productId
     * @param userId
     */
    private void setOperationIgnore(Long productId,Long userId){
        List<ShareOperationRecord> operationRecords = selectOperations(productId,userId,1);

        for (ShareOperationRecord operationRecord : operationRecords){
            operationRecord.setOperationType(3);
            shareOperationRecordMapper.updateByPrimaryKeySelective(operationRecord);
        }
    }

    @Override
    public ShareOperationRecord getOne(Long shareId, Long userId) {
        return shareOperationRecordMapper.selectByShareIdAndUserId(shareId,userId);
    }

    @Override
    public List<ShareOperationRecord> selectOperations(Long product, Long userId, Integer operationStatus) {
        return shareOperationRecordMapper.selectShareOperationRecordByProductIdAndUserId(product,userId,operationStatus);
    }

    @Override
    public boolean updateShareOperationRecord(ShareOperationRecord shareOperationRecord) {
        Integer result = shareOperationRecordMapper.updateByPrimaryKeySelective(shareOperationRecord);
        if (result != 0)
            return true;

        return false;
    }
}
