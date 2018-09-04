package tcy.api.controller.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.utils.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(FileController.class);

    private static String filePath = "/Users/taoran/Pictures/";

    @RequestMapping("/upload/v1.1")
    public ResponseVo upload(@RequestParam("file")MultipartFile file){
        if (file == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        try {
            int fileLength = file.getBytes().length;
            if (fileLength <= 0){
                return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
            }

            String fileName = Utils.getUuid(false)+"_"+file.getOriginalFilename();

            String path = filePath+fileName;

            File newFile = new File(path);
            if (!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdir();
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            bufferedOutputStream.write(file.getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            Map<String,String> result = new HashMap<String, String>();
            result.put("fileName",fileName);

            return ResponseVo.ok(result);
        } catch (IOException e) {
            logger.error("upload file failed cause by {}",e);
            return ResponseVo.error(ResponseCode.FILE_UPLOAD_ERROR);
        }
    }

    @GetMapping("/get/{fileName}")
    public void getFile(@PathVariable String fileName, HttpServletResponse response,@RequestParam("type")String type){
        if (fileName == null){
            return;
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(new File(filePath+fileName+"."+type));
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes))!= -1){
                outputStream.write(bytes,0,length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

}
