package tcy.api.controller.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.User;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController extends BaseController{


    @GetMapping("/not_list/v1.1")
    public ResponseVo NotEvaluatedList(HttpServletRequest request){
        User user = curUser(request);
        if (user.getId() == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }


        return null;
    }

}

