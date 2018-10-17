package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ShareOperationRecord;

import java.util.List;

public interface ShareOperationRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShareOperationRecord record);

    int insertSelective(ShareOperationRecord record);

    ShareOperationRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShareOperationRecord record);

    int updateByPrimaryKey(ShareOperationRecord record);

    List<ShareOperationRecord> listByShareId(@Param("shareId")Long shareId);

    List<ShareOperationRecord> selectShareOperationRecordByProductIdAndUserId(@Param("productId")Long productId,
                                                                              @Param("userId")Long userId,
                                                                              @Param("operationType")Integer operationType);

    ShareOperationRecord selectByShareIdAndUserId(@Param("shareId")Long shareId,@Param("userId")Long userId);
}