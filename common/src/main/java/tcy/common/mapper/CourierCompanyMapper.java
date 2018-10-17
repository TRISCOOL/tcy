package tcy.common.mapper;

import tcy.common.model.CourierCompany;

import java.util.List;

public interface CourierCompanyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CourierCompany record);

    int insertSelective(CourierCompany record);

    CourierCompany selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CourierCompany record);

    int updateByPrimaryKey(CourierCompany record);

    List<CourierCompany> listAll();
}