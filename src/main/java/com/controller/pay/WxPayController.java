package com.controller.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * 2018年2月9日 下午5:42:50
 * 
 * @Author：郭 
 * @Description： 微信支付接口
 *
 */
@Controller
@RequestMapping("/wxPay")
public class WxPayController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {

	}

	/**
	 * 
	 * 2018年2月9日 下午5:47:33
	 * @Author：郭
	 * @param req
	 * @param res
	 * @param code
	 * @Description：
	 */
	@RequestMapping(value = "/pay", method = { RequestMethod.POST, RequestMethod.GET })
	public void pay(HttpServletRequest req, HttpServletResponse res, String code) {
		log.info("注释接口");
		
	}

}
