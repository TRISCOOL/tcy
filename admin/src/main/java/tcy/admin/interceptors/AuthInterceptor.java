package tcy.admin.interceptors;

import tcy.admin.annotations.AuthRequire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import tcy.common.exception.ResponseCode;
import tcy.common.service.RedisService;
import tcy.common.utils.Utils;
import tcy.admin.vo.ResponseVo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


public class AuthInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String AUTHORIZATION = "Authorization";


    @Resource
    private RedisService redisService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AuthRequire auth = handlerMethod.getMethodAnnotation(AuthRequire.class);
            if (auth == null) {
                return true;
            }
            String authorization = request.getHeader(AUTHORIZATION);
            try {
                if (StringUtils.isEmpty(authorization)) {
                    return authFail(response, ResponseCode.AUTH_FAILED);
                }
                if (redisService == null) {
                    redisService = (RedisService) WebApplicationContextUtils.getRequiredWebApplicationContext(request
                            .getServletContext()).getBean(RedisService.class);
                }
                if (!redisService.exists(authorization)) {
                    return authFail(response, ResponseCode.AUTH_FAILED);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return authFail(response, ResponseCode.AUTH_FAILED);
            }
        } else {
            return true;
        }
    }

    private boolean authFail(HttpServletResponse response, ResponseCode errorCode) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (OutputStream outputStream = response.getOutputStream()) {
            ResponseVo vo = ResponseVo.error(errorCode);
            outputStream.write(Utils.toJson(vo).getBytes());
        } catch (IOException e) {
            logger.error("auth fail", e);
            return false;
        }
        return false;
    }
}
