package tcy.admin.controller;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import tcy.common.exception.ResponseCode;
import tcy.common.exception.TcyException;
import tcy.common.model.User;
import tcy.common.service.RedisService;
import tcy.common.utils.Utils;
import tcy.admin.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/base")
@RestController
public class BaseController {

    protected static final String AUTHORIZATION = "Authorization";

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RedisService redisService;

    @ExceptionHandler
    @ResponseBody
    public ResponseVo defaultExceptionHandler(Exception ex) {
        ResponseVo vo = ResponseVo.error(ResponseCode.SERVER_ERROR);
        if (ex instanceof TcyException) {
            TcyException tcyException = (TcyException) ex;
            vo = ResponseVo.error(tcyException.getErrorCode());
        }
        if (ex instanceof BindException) {
            vo = ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        logger.error("{}", ex);
        return vo;
    }

//    protected User curUser(HttpServletRequest request) throws LassException {
//        String authorization = request.getHeader(AUTHORIZATION);
//        User user = null;
//        if (!StringUtils.isEmpty(authorization)) {
//            String userStr = redisService.getStr(authorization);
//            if (!StringUtils.isEmpty(userStr)) {
//                user = gson.fromJson(userStr, User.class);
//            }
//        }
//        return user;
//    }

/*    protected User curUser(HttpServletRequest request){
        User user = new User();
        user.setId(1l);
        user.setExhibitionId(1l);
        return user;
    }*/

    protected User curUser(HttpServletRequest request) throws TcyException {
        String authorization = request.getHeader(AUTHORIZATION);
        User user = new User();
        if (!StringUtils.isEmpty(authorization)) {
            String userStr = redisService.getStr(authorization);
            if (!StringUtils.isEmpty(userStr)) {
                user = Utils.fromJson(userStr, new TypeToken<User>() {
                });
            }
        }
        return user;
    }

    protected User curUser(HttpServletRequest request, boolean checkToken) throws TcyException {
        User user = curUser(request);
        if (user == null && checkToken) {
            throw new TcyException(ResponseCode.AUTH_FAILED);
        }
        return user;
    }

//    protected User curUser(HttpServletRequest request, boolean checkToken) throws LassException {
//        User user = curUser(request);
//        if (user == null && checkToken) {
//            throw new LassException(ErrorCode.AUTH_FAIL);
//        }
//        return user;
//    }

//    public static ResponseVO error(ErrorCode code) {
//        ResponseVO tcy.admin.vo = new ResponseVO(false);
//        if(code != null) {
//            tcy.admin.vo.setMessage(code.getMsg());
//            tcy.admin.vo.setCode(code.getCode());
//        }
//
//        return tcy.admin.vo;
//    }

    @GetMapping("/test")
    public ResponseVo test(){

        Map<String,String> result = new HashMap<String, String>();
        result.put("token","1234");
        result.put("userName","哈哈哈");

        return ResponseVo.ok(result);
    }
}
