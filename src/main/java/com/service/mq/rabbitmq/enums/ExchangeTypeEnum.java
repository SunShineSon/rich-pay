package com.service.mq.rabbitmq.enums;


/**
 * 交换机类型枚举类
 *
 */
public enum ExchangeTypeEnum {

	DIRECT("direct"),
	TOPIC("topic"),
	FANOUT("fanout"),
	HEADERS("headers"),
	SYSTEM("system");
	
	String type;
	
	private ExchangeTypeEnum(String type) {
		this.type = type;
	}

	public String getType(){
		return this.type;
	}
}
