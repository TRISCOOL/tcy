package tcy.api.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.api.controller.BaseController;
import tcy.api.vo.EvaluationCountVo;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.model.Comment;
import tcy.common.model.ProductVo;
import tcy.common.model.User;
import tcy.common.service.EvaluationService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/comment")
@RestController
public class CommentController extends BaseController{

    @Autowired
    private EvaluationService evaluationService;


    /**
     * 获取需要评论列表
     * @param request
     * @return
     */
    @GetMapping("/need/v1.1")
    public ResponseVo getListNeedEvaluation(HttpServletRequest request,
                                            @RequestParam("page") Integer page,
                                            @RequestParam("length") Integer length){
        User user = curUser(request);
        if (user.getId() == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        Integer offset = page -1 < 0?0:page-1;

        List<ProductVo> productVoList = evaluationService.listEvaluateProduct(user.getId(),0,
                offset*length,length);
        return ResponseVo.ok(productVoList);
    }

    /**
     * 新增一条评论
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/add/v1.1")
    public ResponseVo addComment(@ModelAttribute Comment comment,HttpServletRequest request){
        User user = curUser(request);
        if (user.getId() == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        if (comment.getLeavel() == null || comment.getContent() == null || comment.getProductId() == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        comment.setUserId(user.getId());

        boolean result = evaluationService.addEvaluate(comment);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 商品的评论统计title
     * @param productId
     * @return
     */
    @GetMapping("/statistical/v1.1")
    public ResponseVo statisticalComment(@RequestParam("productId")Long productId){

        Map<String,EvaluationCountVo> result = new HashMap<String, EvaluationCountVo>();

        int countSum = evaluationService.countCommentNumByProduct(productId);
        result.put("sum",new EvaluationCountVo(countSum,"sum"));

        int haveImage = evaluationService.countCommentNumForImages(productId);
        result.put("haveImages",new EvaluationCountVo(haveImage,"haveImages"));

        int badReviewCount = evaluationService.countCommentNumForStatus(productId,0);
        result.put("badReview",new EvaluationCountVo(badReviewCount,"badReview"));

        int generalCount = evaluationService.countCommentNumForStatus(productId,1);
        result.put("general",new EvaluationCountVo(generalCount,"general"));

        int praiseCount = evaluationService.countCommentNumForStatus(productId,2);
        result.put("praise",new EvaluationCountVo(praiseCount,"praise"));

        return ResponseVo.ok(result);
    }

    /**
     * 获取每个产品的评价列表
     * @param productId
     * @param page
     * @param length
     * @return
     */
    @GetMapping("/list/v1.1")
    public ResponseVo commentListForProduct(@RequestParam("productId")Long productId,
                                            @RequestParam("page") Integer page,
                                            @RequestParam("length") Integer length){

        Integer offset = page -1 < 0?0:page-1;

        List<Comment> comments = evaluationService.listComment(productId,offset*length,length);
        return ResponseVo.ok(comments);
    }

    /**
     * 获取某个用户的所有评论列表
     * @param page
     * @param length
     * @param request
     * @return
     */
    @GetMapping("/list_user/v1.1")
    public ResponseVo listCommentForUser(@RequestParam("page")Integer page,
                                         @RequestParam("length")Integer length,
                                         HttpServletRequest request){
        User user = curUser(request);
        if (user.getId() == null){
            return ResponseVo.error(ResponseCode.AUTH_FAILED);
        }

        Integer offset = page-1 < 0?0:page-1;


        List<Comment> comments = evaluationService.listCommentsByUser(user.getId(),offset*length,length);
        return ResponseVo.ok(comments);
    }

    /**
     * 删除一条评论
     * @param commentId
     * @return
     */
    @PostMapping("/delete/v1.1")
    public ResponseVo deleteComment(@RequestParam("commentId")Long commentId){
        boolean result = evaluationService.deleteComment(commentId);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

}
