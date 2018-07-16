package tcy.common.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.UserMapper;
import tcy.common.model.User;
import tcy.common.service.PushService;
import tcy.common.utils.Utils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushServiceImpl implements PushService{

    private final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    private final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    //替换成你的AK
    private final String accessKeyId = "43Q0NM47GA0XjFR6";//你的accessKeyId,参考本文档步骤2
    private final String accessKeySecret = "4sEUlBLNJvBCnaZt31ApKGoju0SLU8";//你的accessKeySecret，参考本文档步骤2

    private final String SIGNNAME = "陶然";

    private IClientProfile iClientProfile = null;

    private static Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    private IClientProfile init(){
        if (iClientProfile != null){
            return iClientProfile;
        }
        synchronized (this){
            if (iClientProfile != null){
                return iClientProfile;
            }

            iClientProfile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId,accessKeySecret);
            try {
                DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            } catch (ClientException e) {
                logger.error("init failed cause by {}",e.getErrMsg());
                e.printStackTrace();
            }
            return iClientProfile;
        }
    }

    private void push(String phone,String templateCode,String templateParam){

        logger.info("send message to {},content is {}",phone,templateParam);

        IAcsClient acsClient = new DefaultAcsClient(iClientProfile);
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setMethod(MethodType.POST);
        sendSmsRequest.setPhoneNumbers(phone);
        sendSmsRequest.setSignName(SIGNNAME);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(templateParam);

        try {
            SendSmsResponse response = acsClient.getAcsResponse(sendSmsRequest);
            if(response.getCode() != null && response.getCode().equals("OK")) {//请求成功
                logger.info("send message to {} success",phone);
            }else {
                logger.warn("send message to {} failed,code is {}",phone,response.getCode()+":"+response.getMessage());
            }
        } catch (ClientException e) {
            logger.error("send message to {} failed cause by code is {}",phone,e.getErrCode());
            logger.error("send message to {} failed cause by msg is {}",phone,e.getErrMsg());
        }
    }

    @Override
    public boolean pushVerificationCode(Long userId, String code) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            logger.error("not found this user id is {}",userId);
        }

        Map<String,String> params = new HashMap<String, String>();
        params.put("code",code);

        push(user.getPhone(),"SMS_139860074", Utils.toJson(params));
        return true;
    }

    @Override
    public boolean pushOrderSuccess(Long userId, String orderNumber) {
        return false;
    }

    @Override
    public boolean pushShip(Long userId, String orderNumber) {
        return false;
    }

    @Override
    public boolean pushTradeFinished(Long userId, String orderNumber) {
        return false;
    }

}
