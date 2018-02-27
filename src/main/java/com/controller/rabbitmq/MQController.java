package com.controller.rabbitmq;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.entity.user.User;
import com.service.mq.rabbitmq.enums.RoutingKeyEnum;
import com.service.mq.rabbitmq.template.RabbitMQTemplate;


@Controller
@RequestMapping("/mq")
public class MQController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RabbitMQTemplate rabbitMQTemplate;
	
	/**
	 * mq测试
	 * 2018年2月27日 下午2:18:49
	 * @Author：郭
	 * @param user
	 * @Description：
	 */
	@RequestMapping(value = "/mqMothed", method = { RequestMethod.POST, RequestMethod.GET })
	public void mqMothed(HttpServletRequest req, HttpServletResponse res, User user){
		try {
			log.info("User info : " + user.toString());
			rabbitMQTemplate.send(user, RoutingKeyEnum.REPORT_USER_TEST_ROUTINGKEY.getName());
			res.getWriter().print("true");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

