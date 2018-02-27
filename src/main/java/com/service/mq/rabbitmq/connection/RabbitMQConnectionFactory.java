package com.service.mq.rabbitmq.connection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.service.mq.rabbitmq.constant.RabbitMQConstant;

/**
 * RabbitMQ连接工厂类
 *
 */
@Service
public class RabbitMQConnectionFactory implements DisposableBean{

	private static Logger logger = Logger.getLogger(RabbitMQConnectionFactory.class);
	
	protected Connection connection;
			
	private ConnectionFactory connectionFactory;
	
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	/**
	 * 获取共用的连接
	 */
	public synchronized Connection getConnection() throws Exception {
		try {
			if(connection == null || !connection.isOpen()){
				connection = connectionFactory.newConnection(RabbitMQConstant.getAddrs());
			}
		} catch (Exception e) {
			logger.error("Rabbitmq连接失败！Error message：" + e.getMessage());
        }
		return connection;
	}
	
	public void destroy() throws Exception {
		if (connection != null && connection.isOpen()) {
            connection.close();
        }
	}

}
