package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.City;

import java.util.List;

public interface CityMapper {
    int deleteByPrimaryKey(Long cityId);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Long cityId);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);

    List<City> listCityByProvinceId(@Param("provinceId")Long provinceId,@Param("cityName")String cityName);
}