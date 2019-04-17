package com.zhbit.xuexin.controller.company;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.zhbit.xuexin.common.config.AlipayProperties;
import com.zhbit.xuexin.common.util.ResponseUtil;
import com.zhbit.xuexin.dto.OrderDetailDto;
import com.zhbit.xuexin.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xuexin/security/company/alipay")
public class AlipayController {

    @Autowired
    AlipayService  alipayService;

    @PostMapping("/payment")
    public ResponseEntity notifyPayment(@RequestBody OrderDetailDto orderDetailDto) throws AlipayApiException {
        String body = alipayService.payment(orderDetailDto);
        return ResponseUtil.success(body);
    }


}
