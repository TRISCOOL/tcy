package tcy.common.mapper;

import tcy.common.model.OperationRecord;

public interface OperationRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OperationRecord record);

    int insertSelective(OperationRecord record);

    OperationRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OperationRecord record);

    int updateByPrimaryKey(OperationRecord record);
}