package tcy.api.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.vo.ResponseVo;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    public ResponseVo login(@RequestParam("wx_code")String wxCode){

        //TODO 获取微信认证，获得openId等信息
        //TODO 如果是已有用户就直接返回登录信息，否则插入openId等信息后，再返回用户信息
        return ResponseVo.ok();
    }
}
