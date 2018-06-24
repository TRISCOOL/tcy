package tcy.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.vo.ResponseVo;

@RequestMapping("/base")
@RestController
public class BaseController {

    @GetMapping("/test")
    public ResponseVo test(){
        return ResponseVo.ok();
    }
}
