package tcy.common.mapper;

import org.apache.ibatis.annotations.Param;
import tcy.common.model.Comment;
import tcy.common.model.CommentWithBLOBs;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKeyWithBLOBs(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectCommentsByProductId(@Param("productId")Long productId,
                                            @Param("offset")Integer offset,
                                            @Param("length")Integer length);

    int countCommentNumByProduct(@Param("productId")Long productId);

    int countCommentNumForStatus(@Param("productId")Long productId,@Param("leavel")Integer leavel);

    int countCommentNumForImages(@Param("productId")Long productId);

    List<Comment> listCommentsByUser(@Param("userId")Long userId,
                                     @Param("offset")Integer offset,
                                     @Param("length")Integer length);

    int deleteComment(@Param("commentId")Long commentId);
}