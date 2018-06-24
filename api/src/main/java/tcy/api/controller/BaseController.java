package tcy.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcy.api.vo.ResponseVo;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/base")
@RestController
public class BaseController {

    @GetMapping("/test")
    public ResponseVo test(){

        Map<String,String> result = new HashMap<String, String>();
        result.put("token","1234");
        result.put("userName","哈哈哈");

        return ResponseVo.ok(result);
    }
}
