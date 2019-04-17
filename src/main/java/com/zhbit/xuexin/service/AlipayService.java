package com.zhbit.xuexin.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zhbit.xuexin.common.config.AlipayProperties;
import com.zhbit.xuexin.dto.OrderDetailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlipayService {

    @Autowired
    AlipayProperties alipayProperties;

    public String payment(OrderDetailDto orderDetailDto) {
        String orderNo = new Date().toLocaleString();
        String subject = "会员缴费详情账单";
        String body = "会员缴费项目描述";
        // 封装请求客户端
        String form = ""; // 生成的支付表单
        try {
            AlipayClient client = new DefaultAlipayClient(alipayProperties.getUrl(), alipayProperties.getAppid(), alipayProperties.getRsa_private_key(),
                    alipayProperties.getFormat(), alipayProperties.getCharset(), alipayProperties.getAlipay_public_key(), alipayProperties.getSigntype());
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(alipayProperties.getReturn_url());
            alipayRequest.setNotifyUrl(alipayProperties.getNotify_url());
            AlipayTradePayModel model = new AlipayTradePayModel();
            model.setProductCode("FAST_INSTANT_TRADE_PAY"); // 设置销售产品码
            model.setOutTradeNo(orderNo); // 设置订单号
            model.setSubject(subject); // 订单名称
            model.setTotalAmount(orderDetailDto.getTotalFee().toString()); // 支付总金额
            model.setBody(body); // 设置商品描述
            alipayRequest.setBizModel(model);
            form = client.pageExecute(alipayRequest).getBody(); // 生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }
}
