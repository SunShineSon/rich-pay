package com.service.mq.rabbitmq.enums;

/**
 * 路由键定义枚举类
 */
public enum RoutingKeyEnum {
	
	DEFAULT_ROUTINGKEY("default.routingKey"),
	
	REPORT_USER_TEST_ROUTINGKEY("report.user.test.routingKey"),
	
	PROFIT_STEP("profit.step.routingKey");
	
	String name;
	
	private RoutingKeyEnum(String name) {
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
