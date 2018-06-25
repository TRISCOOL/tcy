package tcy.api.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.vo.ResponseCode;
import tcy.api.vo.ResponseVo;
import tcy.api.vo.WxSPEncryptedData;
import tcy.api.vo.WxSPRespVo;
import tcy.common.model.User;
import tcy.common.service.UserService;
import tcy.common.utils.AES;
import tcy.common.utils.HttpClientUtils;
import tcy.common.utils.StringUtils;
import tcy.common.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("#{environment.wxspappid}")
    private String WxSPAppid = "wxd974c1e2284fb15b";

    @Value("#{environment.wxspsecret}")
    private String WxSPSecret = "b58227d8b425b06c17d4f7bff2f4f4d9";

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
     * @param wxCode
     * @param encryptedData
     * @param iv
     * @return
     */
    @PostMapping("/login")
    public ResponseVo login(@RequestParam("wxCode")String wxCode,
                            @RequestParam("encryptedData")String encryptedData,
                            @RequestParam("iv") String iv){

        //TODO 获取微信认证，获得openId等信息
        WxSPRespVo wxSPRespVo = getSeesionKey(wxCode);
        if(null == wxSPRespVo){
            logger.error("get seesionKey is failed");
            return ResponseVo.error(ResponseCode.SERVER_ERROR);
        }

        WxSPEncryptedData userInfo = decodeEncryptedDataData(encryptedData, wxSPRespVo.getSession_key(), iv);
        if(userInfo == null){
            return ResponseVo.error(ResponseCode.SERVER_ERROR);
        }

        //TODO 如果是已有用户就直接返回登录信息，否则插入openId等信息后，再返回用户信息
        User user = packagingWithWxSPEncryptedData(userInfo);
        User result = userService.loginAndRegister(user);

        return ResponseVo.ok(result);
    }

    private User packagingWithWxSPEncryptedData(WxSPEncryptedData wxUser){
        User user = new User();
        user.setWxName(wxUser.getNickName());
        user.setOpenId(wxUser.getOpenId());
        user.setSex(wxUser.getGender().equals("男")?1:2);

        return user;
    }
}
