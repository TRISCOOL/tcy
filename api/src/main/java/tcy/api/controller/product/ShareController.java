package tcy.api.controller.product;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.api.annotations.AuthRequire;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.ShareOperationRecord;
import tcy.common.model.ShareRecord;
import tcy.common.model.User;
import tcy.common.service.ShareService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/share")
public class ShareController extends BaseController{

    @Autowired
    private ShareService shareService;

    /**
     * 在分享商品时，需记录本次分享主体信息
     * @param request
     * @param shareRecord
     * @return
     */
    @PostMapping("/record/v1.1")
    public ResponseVo recordShare(HttpServletRequest request, ShareRecord shareRecord){
        User user = curUser(request);
        if (user.getId() == null)
            return ResponseVo.error(ResponseCode.AUTH_FAILED);


        if (shareRecord.getProductId() == null)
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);

        shareRecord.setCreateTime(new Date());
        shareRecord.setUserId(user.getId());
        Long shareId = shareService.recordShare(shareRecord);
        if (shareId != null) {
            Map<String,Long> result = new HashMap<String, Long>();
            result.put("shareId",shareId);
            return ResponseVo.ok(result);
        }
        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 分享列表
     * @param request
     * @return
     */
    @GetMapping("/list/v1.1")
    public ResponseVo listShare(HttpServletRequest request){
        User user = curUser(request);

        List<ShareRecord> records = shareService.listShareForUser(user.getId());
        return ResponseVo.ok(records);
    }

    /**
     * 分享被操作详情
     * @param shareId
     * @return
     */
    @GetMapping("/details/v1.1")
    public ResponseVo getDetails(@RequestParam("shareId")Long shareId){
        return ResponseVo.ok(shareService.listOperationRecord(shareId));
    }

    /**
     *
     * @param request
     * @param shareOperationRecord operationType 1-查看
     * @return
     */
    @PostMapping("/operation/v1.1")
    @AuthRequire
    public ResponseVo recordOperation(HttpServletRequest request, @RequestBody ShareOperationRecord shareOperationRecord){
        User user = curUser(request);
        if (user.getId()  == null)
            return ResponseVo.error(ResponseCode.AUTH_FAILED);

        shareOperationRecord.setUserId(user.getId());
        shareService.updateOrInsertOneOperationRecord(shareOperationRecord);
        return ResponseVo.ok();
    }
}
