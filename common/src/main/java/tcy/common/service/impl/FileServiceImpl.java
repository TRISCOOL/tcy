package tcy.common.service.impl;

import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tcy.common.service.FileService;

@Service
public class FileServiceImpl implements FileService{

    @Value("#{environment.QINIU_ACCESSKEY}")
    String accessKey = "idaGDJRhRq9ZlfU8NnQW5QP6qXvZg3VEIY_HdH8i";

    @Value("#{environment.QINIU_SECRETKEY}")
    String secretKey = "IdVr72qfquNebWONSWfB-4kidulnufyljQKyYM7q";

    private static final String BUCKET = "tcy-images";//7niu文件夹的名字


    @Override
    public String getQiNiuToken() {
        Auth auth = Auth.create(accessKey,secretKey);
        String token = auth.uploadToken(BUCKET);
        return token;
    }
}
