package tcy.api.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.*;
import tcy.common.service.AddressService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController{

    @Autowired
    private AddressService addressService;

    /**
     * 获取中国省份
     * @param name
     * @return
     */
    @GetMapping("/province/v1.1")
    public ResponseVo getProvinceList(@RequestParam(value = "name",required = false)String name){
        if ("".equals(name)){
            name = null;
        }

        List<Province> provinces = addressService.listProvinces(name);
        return ResponseVo.ok(provinces);
    }

    /**
     * 根据省份id获取城市
     * @param provinceId
     * @param name
     * @return
     */
    @GetMapping("/city/v1.1")
    public ResponseVo getCityList(@RequestParam("provinceId")Long provinceId,
                                  @RequestParam(value = "name",required = false)String name){
        if (provinceId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        if ("".equals(name)){
            name = null;
        }

        List<City> cities = addressService.listCitys(provinceId,name);
        return ResponseVo.ok(cities);
    }

    /**
     * 根据城市id获取区县
     * @param cityId
     * @param name
     * @return
     */
    @GetMapping("/district/v1.1")
    public ResponseVo getDistrictList(@RequestParam("cityId")Long cityId,
                                      @RequestParam(value = "name",required = false)String name){

        List<District> districts = addressService.listDistrict(cityId,name);
        return ResponseVo.ok(districts);
    }

    /**
     * 添加收件人
     * @param address
     * @return
     */
    @PostMapping("/add/v1.1")
    public ResponseVo addAddress(@ModelAttribute Address address, HttpServletRequest request){

        User user = curUser(request);

        if (address == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        if (address.getProvince() == null || address.getCity() == null || address.getCounty() == null
                || address.getDetailAddress() == null || address.getContact() == null
                || address.getContactPhone() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        address.setDeleted(0);
        address.setUserId(user.getId());

        boolean result = addressService.addAddress(address);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 获取收件人列表
     * @param request
     * @return
     */
    @GetMapping("/list/v1.1")
    public ResponseVo listAddress(HttpServletRequest request){
        User user = curUser(request);
        if (user == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        List<Address> addresses = addressService.listAddress(user.getId());
        return ResponseVo.ok(addresses);
    }

    /**
     * 更新收件人信息
     * @param address
     * @return
     */
    @PostMapping("/update/v1.1")
    public ResponseVo updateAddress(@ModelAttribute Address address){
        if (address == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        if (address.getId() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        boolean result = addressService.updateAddress(address);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 设置默认地址
     * @param address
     * @return
     */
    @PostMapping("/default/v1.1")
    public ResponseVo setDefaultAddress(@ModelAttribute Address address){
        if (address == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        if (address.getId() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        boolean result = addressService.setDefaultAddress(address.getId());
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 删除收件人
     * @param addressId
     * @return
     */
    @PostMapping("/delete/v1.1")
    public ResponseVo deleteAddress(@RequestParam("addressId")Long addressId){
        boolean result = addressService.deletedAddress(addressId);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }


}
