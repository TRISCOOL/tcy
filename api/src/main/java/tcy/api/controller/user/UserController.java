package tcy.api.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.api.vo.WxRequest;
import tcy.api.vo.WxSPEncryptedData;
import tcy.api.vo.WxSPRespVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.User;
import tcy.common.service.UserService;
import tcy.common.utils.AES;
import tcy.common.utils.HttpClientUtils;
import tcy.common.utils.StringUtils;
import tcy.common.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Value("#{environment.wxspappid}")
    private String WxSPAppid = "wx8b2b0165d2e6c115";

    @Value("#{environment.wxspsecret}")
    private String WxSPSecret = "60a1a9b31cb786b0ba180aead94313f3";

    @Value("#{environment.wxspsecret}")
    private String WxSPurl = "https://api.weixin.qq.com/sns/jscode2session";

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private WxSPRespVo getSeesionKey(String code) {
        String url = WxSPurl + "?grant_type=authorization_code&appid=" + WxSPAppid + "&secret=" + WxSPSecret + "&js_code=" + code;
        String responStr = null;
        try {
            responStr = HttpClientUtils.get(url);
            logger.info("wx Auth :{}",responStr);
            WxSPRespVo wxSPRespVo = Utils.gson.fromJson(responStr, WxSPRespVo.class);
            if(wxSPRespVo.getErrcode() == null && wxSPRespVo.getOpenid() != null && wxSPRespVo.getSession_key() != null){
                return wxSPRespVo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private WxSPEncryptedData decodeEncryptedDataData(String encryptedData, String session_key, String iv){
        Map map = new HashMap();
        WxSPEncryptedData userInfo = null;
        try {
            byte[] resultByte  = AES.decrypt(decodeBase64(encryptedData),
                    decodeBase64(session_key),
                    decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                String deStr = new String(resultByte, "UTF-8");
                logger.info("userinfo:{}", deStr);
                userInfo = StringUtils.gson.fromJson(deStr, WxSPEncryptedData.class);
                return userInfo;
            }else{
                logger.info("decode wxsp data is failed");
            }
            return null;
        }catch (InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信用户登录
     * @param wxRequest
     * @return
     */
    @PostMapping("/login/v1.1")
    public ResponseVo login(@RequestBody WxRequest wxRequest){

        //TODO 获取微信认证，获得openId等信息
        WxSPRespVo wxSPRespVo = getSeesionKey(wxRequest.getWxCode());
        if(null == wxSPRespVo){
            logger.error("get seesionKey is failed");
            return ResponseVo.error(ResponseCode.SERVER_ERROR);
        }

        //WxSPEncryptedData userInfo = decodeEncryptedDataData(w*/xRequest.getEncryptedData(), wxSPRespVo.getSession_key(), wxRequest.getIv());
/*        if(userInfo == null){*/
/*            return ResponseVo.error(ResponseCode.SERVER_ERROR)*/;
/*        }*/
/*
*/

/*        //TODO 如果是已有用户就直接返回登录信息，否则插入openId等信息后，再返回用户信息*/
/*        User user = packagingWithWxSPEncryptedData(userInfo);*/
        User user = new User();
        user.setOpenId(wxSPRespVo.getOpenid());
        user.setSex(Integer.parseInt(wxRequest.getWxGender()));
        user.setLogo(wxRequest.getUrl());
        user.setWxName(wxRequest.getWxNickName());

        User result = userService.loginAndRegister(user);

        return ResponseVo.ok(result);
    }

    @PostMapping("/bind_phone/v1.1")
    public ResponseVo bindPhone(@RequestParam("phone")String phone, HttpServletRequest request){
        if (phone == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        User user = curUser(request);
        if (user == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        user.setPhone(phone);
        boolean result = userService.updateUser(user);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    private User packagingWithWxSPEncryptedData(WxSPEncryptedData wxUser){
        User user = new User();
        user.setWxName(wxUser.getNickName());
        user.setOpenId(wxUser.getOpenId());
        user.setSex(wxUser.getGender().equals("男")?1:2);

        return user;
    }
}
