package tcy.common.mapper;

public interface RecommendSubMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RecommendSub record);

    int insertSelective(RecommendSub record);

    RecommendSub selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RecommendSub record);

    int updateByPrimaryKey(RecommendSub record);
}