package com.service.mq.rabbitmq.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.entity.user.User;

public class ReportDelegate {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * mq消费者方法
	 * 2018年2月27日 下午2:34:30
	 * @Author：郭
	 * @param user
	 * @Description：
	 */
	public void reportUser(User user){
		log.info("mq 消费者，用户数据：" + user.toString());
		
	}

}

