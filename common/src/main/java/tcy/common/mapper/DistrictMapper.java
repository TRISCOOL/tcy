package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.District;

import java.util.List;

public interface DistrictMapper {
    int deleteByPrimaryKey(Long districtId);

    int insert(District record);

    int insertSelective(District record);

    District selectByPrimaryKey(Long districtId);

    int updateByPrimaryKeySelective(District record);

    int updateByPrimaryKey(District record);

    List<District> listDistrictByCityId(@Param("cityId")Long cityId,@Param("districtName")String districtName);
}