package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Province;

import java.util.List;

public interface ProvinceMapper {
    int deleteByPrimaryKey(Long provinceId);

    int insert(Province record);

    int insertSelective(Province record);

    Province selectByPrimaryKey(Long provinceId);

    int updateByPrimaryKeySelective(Province record);

    int updateByPrimaryKey(Province record);

    List<Province> listProvince(@Param("provinceName")String provinceName);
}