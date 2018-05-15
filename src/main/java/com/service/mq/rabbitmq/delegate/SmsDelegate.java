package com.service.mq.rabbitmq.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.entity.message.MessageSmsVo;
import com.service.inter.ShortMessageService;

import net.sf.json.JSONObject;


/**
 * 短信消息发送
 * 2018年5月11日 下午2:34:15
 * 
 * @Author：郭
 * @Description： 
 *
 */
public class SmsDelegate {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ShortMessageService shortMessageService;
	
	/**
	 * 短信息消费者，发送段信息
	 * 2018年5月11日 下午3:24:45
	 * @Author：郭
	 * @param sms
	 * @Description：
	 */
	public void messageSend(String sms){
		JSONObject object = JSONObject.fromObject(sms);
		MessageSmsVo msg = (MessageSmsVo) JSONObject.toBean(object, MessageSmsVo.class);
		log.info("消息系统，mq消费者，数据：" + msg.toString());
		shortMessageService.sendMessage(msg.getPhone(), msg.getArgs(), msg.getProjectId());
		
		
	}

}

