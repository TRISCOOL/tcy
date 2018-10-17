package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.ShareRecord;

import java.util.List;

public interface ShareRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShareRecord record);

    int insertSelective(ShareRecord record);

    ShareRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShareRecord record);

    int updateByPrimaryKey(ShareRecord record);

    ShareRecord selectShareRecord(@Param("userId") Long userId, @Param("productId") Long productId);

    List<ShareRecord> listShareRecord(@Param("userId")Long userId);
}