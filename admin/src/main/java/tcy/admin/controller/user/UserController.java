package tcy.admin.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcy.admin.controller.BaseController;
import tcy.admin.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.AdminUser;
import tcy.common.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping("/login/v1.1")
    public ResponseVo loginAdmin(AdminUser adminUser){
        if (adminUser.getPassword() == null || adminUser.getAccount() == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        AdminUser res = userService.loginAdmin(adminUser);
        return ResponseVo.ok(res);
    }
}
