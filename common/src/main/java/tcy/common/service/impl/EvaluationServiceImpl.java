package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.CommentMapper;
import tcy.common.mapper.ProductMapper;
import tcy.common.model.Comment;
import tcy.common.model.Product;
import tcy.common.model.ProductVo;
import tcy.common.service.EvaluationService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvaluationServiceImpl implements EvaluationService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<ProductVo> listEvaluateProduct(Long userId, Integer isAppraise,Integer offset,Integer length) {

        List<Product> productList = productMapper.selectProductForEvaluated(userId,isAppraise,offset,length);
        List<ProductVo> productVoList = productList.stream().map(product -> {

            ProductVo vo = ProductVo.getVo(product);
            return vo;
        }).collect(Collectors.toList());

        return productVoList;
    }

    @Override
    public boolean addEvaluate(Comment comment) {

        comment.setCreateTime(new Date());
        int result = commentMapper.insertSelective(comment);
        if (result != 0){
            return true;
        }

        return false;
    }

    @Override
    public List<Comment> listComment(Long productId,Integer offset,Integer length) {
        return commentMapper.selectCommentsByProductId(productId,offset,length);
    }

    @Override
    public int countCommentNumByProduct(Long prdouctId) {
        return commentMapper.countCommentNumByProduct(prdouctId);
    }

    @Override
    public int countCommentNumForStatus(Long productId, Integer status) {
        return commentMapper.countCommentNumForStatus(productId,status);
    }

    @Override
    public int countCommentNumForImages(Long productId) {
        return commentMapper.countCommentNumForImages(productId);
    }

    @Override
    public List<Comment> listCommentsByUser(Long userId,Integer offset,Integer length) {
        return commentMapper.listCommentsByUser(userId,offset,length);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        int result = commentMapper.deleteComment(commentId);
        if (result != 0){
            return true;
        }
        return false;
    }
}
