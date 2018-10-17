package tcy.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcy.common.mapper.*;
import tcy.common.model.*;
import tcy.common.service.IntegralService;
import tcy.common.service.ProductService;

import java.util.List;

@Service
public class IntegralServiceImpl implements IntegralService{

    @Autowired
    private ProductService productService;

    @Autowired
    private IntegralConfigMapper integralConfigMapper;

    @Autowired
    private IntegralRecordMapper integralRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private ShareRecordMapper shareRecordMapper;

    @Override
    public void updateIntegralForUserByOrder(Order order) {
        List<Product> productList = productService.listProductByOrderId(order.getId());
        Integer sumIntegral = 0;
        for (Product p:productList){
            IntegralConfig integralConfig = integralConfigMapper.selectByPrimaryKey(p.getIntegralConfigId());
            if (integralConfig != null){
                sumIntegral += integralConfig.getScore();
            }
        }
        User user = userMapper.selectUserByIdForScore(order.getUserId());
        Score score = new Score();
        score.setId(user.getScoreId());
        score.setUserId(user.getId());
        score.setValue(getNewIntegral(user.getScoreValue(),sumIntegral,1));
        updateIntegral(score);
    }

    @Override
    public void updateIntegralForUserByMoney(IntegralRecord integralRecord) {
        if (integralRecord == null)
            return;

        if (integralRecord.getOperationType() != 3)
            return;

        User user = userMapper.selectUserByIdForScore(integralRecord.getUserId());
        Score score = new Score();
        score.setId(user.getScoreId());
        score.setUserId(user.getId());
        score.setValue(getNewIntegral(user.getScoreValue(),integralRecord.getIntegralValue().intValue(),2));
        updateIntegral(score);
    }

    @Override
    public void updateIntegralForUserByShare(ShareOperationRecord shareOperationRecord) {
        if (shareOperationRecord == null)
            return;

        if (shareOperationRecord.getOperationType() != 2)
            return;

        ShareRecord shareRecord = shareRecordMapper.selectByPrimaryKey(shareOperationRecord.getShareId());
        Product product = productService.onlyGetProductById(shareRecord.getProductId());

        User user = userMapper.selectUserByIdForScore(shareRecord.getUserId());
        Score score = new Score();
        score.setId(user.getScoreId());
        score.setUserId(user.getId());
        score.setValue(getNewIntegral(user.getScoreValue(),product.getScoreValue(),1));
        updateIntegral(score);
    }

    /**
     *
     * @param oldIntegral
     * @param updateIntegral
     * @param type 1-增加，2-减少
     * @return
     */
    private Integer getNewIntegral(Integer oldIntegral,Integer updateIntegral,Integer type){
        if (type == 1){
            return oldIntegral + updateIntegral;
        }else if (type == 2){
            return oldIntegral - updateIntegral;
        }

        return oldIntegral;
    }

    private void updateIntegral(Score score){
        scoreMapper.updateByPrimaryKeySelective(score);
    }
}
