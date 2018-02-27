package com.service.mq.rabbitmq.enums;

/**
 * 队列定义枚举类
 *
 */
public enum QueueEnum {
	
	DEFAULTQUEUE("default.queue");
	
	String name;
	
	private QueueEnum(String name) {
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
