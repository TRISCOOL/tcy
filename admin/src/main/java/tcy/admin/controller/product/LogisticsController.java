package tcy.admin.controller.product;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.admin.controller.BaseController;
import tcy.admin.vo.ResponseVo;
import tcy.common.dto.LogisticsResponseDTO;
import tcy.common.dto.LogisticsTracesResponseDTO;
import tcy.common.exception.ResponseCode;
import tcy.common.model.Order;
import tcy.common.service.OrderService;
import tcy.common.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/logistics")
public class LogisticsController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    /**
     * 待发货订单列表
     * @param request
     * @return
     */
    @GetMapping("/wait_send/v1.1")
    public ResponseVo getWaitSendList(HttpServletRequest request,
                                      @RequestParam("page")Integer page,
                                      @RequestParam("size")Integer size){
        Long shopId = 1L;

        Integer offset = page - 1 < 0 ? 0: page-1;

        List<Order> waitSendList = orderService.listOrderByStatusAndShop(shopId,2,offset*size,size);
        return ResponseVo.ok(waitSendList);
    }

    /**
     * 订单商品详情
     * @param orderId
     * @return
     */
    @GetMapping("/order_details/v1.1")
    public ResponseVo getOrderDetails(@RequestParam("orderId")Long orderId){
        return ResponseVo.ok(productService.listProductByOrderId(orderId));
    }

    /**
     * 快递公司列表
     * @return
     */
    @GetMapping("/courier_list/v1.1")
    public ResponseVo listCourierCompanies(){
        return ResponseVo.ok(orderService.listCourierCompanies());
    }

    /**
     * 录入运单号
     * @param expressNo
     * @param orderId
     * @return
     */
    @PostMapping("/enter/v1.1")
    public ResponseVo enterExpressDelivery(@RequestParam("expressNo")String expressNo,
                                           @RequestParam("orderId")Long orderId,
                                           @RequestParam("courierId")Long courierId){

        Order order = orderService.selectOrderById(orderId);
        if (order == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        order.setWaybillNumber(expressNo);
        order.setStatus(3);
        order.setCourierCompanyId(courierId);
        boolean result = orderService.updateByPrimaryKeySelective(order);
        if (result)
            return ResponseVo.ok();

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 根据订单号查询物流轨迹
     * @return
     */
    @GetMapping("/logistics_traces/v1.1")
    public ResponseVo getLogisticsTraces(@RequestParam("orderId")Long orderId){
        LogisticsResponseDTO response = orderService.logisticsDetails(orderId);
        if (response == null){
            return ResponseVo.error(ResponseCode.NOT_FOUND_LOGISTICS_DETAILS);
        }

        List<LogisticsTracesResponseDTO> traces = response.getTraces();
        if (traces == null){
            return ResponseVo.ok(response.getReason());
        }

        if (traces.size() <= 0){
            return ResponseVo.ok(response.getReason());
        }

        return ResponseVo.ok(traces);
    }


}
