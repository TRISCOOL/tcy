package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcy.common.exception.TcyException;
import tcy.common.mapper.AddressMapper;
import tcy.common.mapper.CityMapper;
import tcy.common.mapper.DistrictMapper;
import tcy.common.mapper.ProvinceMapper;
import tcy.common.model.Address;
import tcy.common.model.City;
import tcy.common.model.District;
import tcy.common.model.Province;
import tcy.common.service.AddressService;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private ProvinceMapper provinceMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private DistrictMapper districtMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public List<Province> listProvinces(String provinceName) {
        return provinceMapper.listProvince(provinceName);
    }

    @Override
    public List<City> listCitys(Long provinceId,String cityName) {
        return cityMapper.listCityByProvinceId(provinceId,cityName);
    }

    @Override
    public List<District> listDistrict(Long cityId,String districtName) {
        return districtMapper.listDistrictByCityId(cityId,districtName);
    }

    @Override
    public boolean addAddress(Address address) {
        int result = addressMapper.insertSelective(address);
        if (result != 0){
            return true;
        }
        return false;
    }

    @Override
    public List<Address> listAddress(Long userId) {
        return addressMapper.selectAddressByUser(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return addressMapper.selectDefaultAddress(userId);
    }

    @Override
    public boolean deletedAddress(Long addressId) {
        int result = addressMapper.deleteAddressById(addressId);
        if (result != 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAddress(Address address) {
        int result = addressMapper.updateByPrimaryKeySelective(address);
        if (result != 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean setDefaultAddress(Long addressId) {
        int result = addressMapper.upAddressTagByUser(-1+"",addressId);
        if (result == 0){
            throw new TcyException("update default address failed 1");
        }

        Address address = new Address();
        address.setId(addressId);
        address.setTag(1+"");

        int result2 = addressMapper.updateByPrimaryKeySelective(address);
        if (result2 == 0){
            throw new TcyException("update default address failed 2");
        }
        return true;
    }
}
