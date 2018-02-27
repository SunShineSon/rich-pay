package com.service.mq.rabbitmq.enums;


/**
 * 交换机定义枚举类
 *
 */
public enum ExchangeEnum {
	
	DEFAULTEXCHANGE("default.exchange");
	
	String name;
	
	private ExchangeEnum(String name) {
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
