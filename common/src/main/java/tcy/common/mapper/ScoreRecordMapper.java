package tcy.common.mapper;

import tcy.common.model.ScoreRecord;

public interface ScoreRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ScoreRecord record);

    int insertSelective(ScoreRecord record);

    ScoreRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ScoreRecord record);

    int updateByPrimaryKey(ScoreRecord record);
}