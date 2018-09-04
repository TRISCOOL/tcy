package tcy.admin.controller.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcy.admin.controller.BaseController;
import tcy.admin.vo.ResponseVo;
import tcy.common.service.FileService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController{

    @Autowired
    private FileService fileService;

    @GetMapping("/auth/v1.1")
    public ResponseVo authForFile(){
        String token = fileService.getQiNiuToken();
        Map<String,String> result = new HashMap<String, String>();
        result.put("qn-token",token);
        return ResponseVo.ok(result);
    }
}
