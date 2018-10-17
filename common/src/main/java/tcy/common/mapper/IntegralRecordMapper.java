package tcy.common.mapper;

import tcy.common.model.IntegralRecord;

public interface IntegralRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IntegralRecord record);

    int insertSelective(IntegralRecord record);

    IntegralRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IntegralRecord record);

    int updateByPrimaryKey(IntegralRecord record);
}