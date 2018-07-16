package tcy.common.service;

import tcy.common.model.Comment;
import tcy.common.model.ProductVo;

import java.util.List;

public interface EvaluationService {

    /**
     *
     * @param userId
     * @param isAppraise 是否评价（0-未评价，1-评价)
     * @return
     */
    List<ProductVo> listEvaluateProduct(Long userId,Integer isAppraise,Integer offset,Integer length);

    boolean addEvaluate(Comment comment);

    List<Comment> listComment(Long productId,Integer offset,Integer length);

    int countCommentNumByProduct(Long prdouctId);

    /**
     *
     * @param productId
     * @param status 评论等级0-差评，1-一般，2-好评
     * @return
     */
    int countCommentNumForStatus(Long productId,Integer status);

    int countCommentNumForImages(Long productId);

    List<Comment> listCommentsByUser(Long userId,Integer offset,Integer length);

    boolean deleteComment(Long commentId);

}
