package com.service.mq.rabbitmq.constant;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.rabbitmq.client.Address;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

public class RabbitMQConstant {
	
	private static final String PROPERTIES = "rabbitmq.properties";
	
	private static String HOSTS;
	
	private static Integer PORT;
		
	private static String USERNAME;
	
	private static String PASSWORD;
	
	private static Address[] ADDRS;

	//是否是持久化的queue
	private static Boolean DURABLE;
	
	//当消费者断开时，是否删除队列
	private static Boolean EXCLUSIVE;
	
	//当绑定的队列都不使用时，是否自动删除交换机
	private static Boolean AUTODELETE;
	
	//是否自动确认消息,false为手动确认
	private static Boolean AUTOACK;
	
	static {
		
		Properties properties = new Properties();

        try {
            Resource res = new ClassPathResource(PROPERTIES);
            properties.load(res.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load rabbitmq.properties!");
        }
        
        HOSTS = properties.getProperty("rabbitmq.hosts");
        PORT = Integer.valueOf(properties.getProperty("rabbitmq.port"));
        USERNAME = properties.getProperty("rabbitmq.userName");
        PASSWORD = properties.getProperty("rabbitmq.password");
        DURABLE = Boolean.valueOf(properties.getProperty("rabbitmq.durable")) ;
        EXCLUSIVE = Boolean.valueOf(properties.getProperty("rabbitmq.exclusive")) ;
        AUTODELETE = Boolean.valueOf(properties.getProperty("rabbitmq.autoDelete"));
        AUTOACK = Boolean.valueOf(properties.getProperty("rabbitmq.autoAck"));
        
        String[] hostArray = HOSTS.split(",");
        List<Address> addresses = new ArrayList<Address>();
        for (String host : hostArray) {
            addresses.add(Address.parseAddress(host));
        }
        ADDRS = addresses.toArray(new Address[hostArray.length]);
        
	}
    
	public static Boolean getDurable() {
		return DURABLE;
	}
	
	public static Boolean getExclusive() {
		return EXCLUSIVE;
	}
	
	public static Boolean getAutoDelete() {
		return AUTODELETE;
	}
	
	public static Boolean getAutoAck() {
		return AUTOACK;
	}
	
	public static Address[] getAddrs() {
		return ADDRS;
	}
}
