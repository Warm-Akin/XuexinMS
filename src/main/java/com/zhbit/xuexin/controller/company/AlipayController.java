package com.zhbit.xuexin.controller.company;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zhbit.xuexin.common.config.AlipayProperties;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.model.Order;
import com.zhbit.xuexin.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/xuexin/security/company/alipay")
public class AlipayController {

    @Autowired
    AlipayProperties alipayProperties;

    @Autowired
    AlipayService  alipayService;

//    @RequestMapping("/payment")
    /*
    public void pay(Order order, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        String orderNo = "20192019";
//        String orderNo = DateUtil.getCurrentDateStr(); // 生成订单号
        String totalAmount = ""; // 支付总金额
        String subject = ""; // 订单名称
        String body = ""; // 商品描述
        switch (order.getProductId()) {
            case 1:
                totalAmount = "50";
                subject = "上交水电费";
                body = "50块的水电费";
                break;
            case 2:
                totalAmount = "100";
                subject = "上交水电费";
                body = "100块的水电费";
                break;
            default:
                totalAmount = "上交水电费";
                subject = "上交水电费";
                body = "50块的水电费";
                break;
        }
        order.setOrderNo(orderNo);
        order.setSubject(subject);
        order.setTotalAmount(totalAmount);
        order.setBody(body);
//        orderService.save(order);
        // 封装请求客户端
        AlipayClient client = new DefaultAlipayClient(alipayProperties.getUrl(), alipayProperties.getAppid(), alipayProperties.getRsa_private_key(), alipayProperties.getFormat(), alipayProperties.getCharset(), alipayProperties.getAlipay_public_key(), alipayProperties.getSigntype());
        String form = ""; // 生成的支付表单
        String userAgent = request.getHeader("user-agent");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayProperties.getReturn_url());
        alipayRequest.setNotifyUrl(alipayProperties.getNotify_url());
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setProductCode("FAST_INSTANT_TRADE_PAY"); // 设置销售产品码
        model.setOutTradeNo(orderNo); // 设置订单号
        model.setSubject(subject); // 订单名称
        model.setTotalAmount(totalAmount); // 支付总金额
        model.setBody(body); // 设置商品描述
        alipayRequest.setBizModel(model);
        form = client.pageExecute(alipayRequest).getBody(); // 生成表单
        System.out.println(form);
        response.setContentType("text/html;charset=" + alipayProperties.getCharset());
        response.getWriter().write(form); // 直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
//        map.put("success", true);
//        return map;
    }

    */


    @RequestMapping("/returnUrl")
    public void returnUrl(HttpServletRequest request, HttpServletResponse response)throws Exception{
//   Order order = orderService.getLastOrder();
//   order.setBuyTime(new Date());
//   order.setIsPay(1);
//   orderService.save(order);

        response.sendRedirect("/main.html");
    }


    @PostMapping("/payment")
    public ResponseEntity notifyPayment(@RequestBody Order order) throws AlipayApiException {
        String body = alipayService.payment(order);
        return ResponseUtil.success(body);
    }


}
