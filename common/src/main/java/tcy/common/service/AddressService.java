package tcy.common.service;

import tcy.common.model.Address;
import tcy.common.model.City;
import tcy.common.model.District;
import tcy.common.model.Province;

import java.util.List;

public interface AddressService {

    /**
     * 获取省列表
     * @return
     */
    List<Province> listProvinces(String provinceName);

    /**
     * 获取城市列表
     * @param provinceId
     * @return
     */
    List<City> listCitys(Long provinceId,String cityName);

    /**
     * 获取区县列表
     * @param cityId
     * @return
     */
    List<District> listDistrict(Long cityId,String districtName);

    /**
     * 增加地址
     * @param address
     * @return
     */
    boolean addAddress(Address address);

    /**
     * 地址列表
     * @param userId
     * @return
     */
    List<Address> listAddress(Long userId);

    /**
     * 获取默认地址
     * @param userId
     * @return
     */
    Address getDefaultAddress(Long userId);

    /**
     * 删除某一条地址
     * @param addressId
     * @return
     */
    boolean deletedAddress(Long addressId);

    /**
     * 更新地址
     * @param address
     * @return
     */
    boolean updateAddress(Address address);

    /**
     * 设置默认地址
     * @param addressId
     * @return
     */
    boolean setDefaultAddress(Long addressId);



}
