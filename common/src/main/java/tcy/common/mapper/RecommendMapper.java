package tcy.common.mapper;

import tcy.common.model.Recommend;

public interface RecommendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Recommend record);

    int insertSelective(Recommend record);

    Recommend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Recommend record);

    int updateByPrimaryKey(Recommend record);
}