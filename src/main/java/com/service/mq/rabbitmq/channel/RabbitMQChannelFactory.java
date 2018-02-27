package com.service.mq.rabbitmq.channel;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.service.mq.rabbitmq.connection.RabbitMQConnectionFactory;

/**
 * rabbitmq的channel工厂类
 * 
 */
@Component
public class RabbitMQChannelFactory implements DisposableBean {

	private static Logger logger = Logger.getLogger(RabbitMQChannelFactory.class);

	private RabbitMQConnectionFactory rabbitMQConnectionFactory;

	private final Set<Reference<Channel>> channelReferenceSet = new HashSet<Reference<Channel>>();

	public void setRabbitMQConnectionFactory(RabbitMQConnectionFactory connectionFactory) {
		this.rabbitMQConnectionFactory = connectionFactory;
	}

	/**
	 * 创建一个channel
	 * 
	 */
	public Channel createChannel() {
		Channel channel = null;
		try {
			if (rabbitMQConnectionFactory.getConnection() != null) {
				channel = rabbitMQConnectionFactory.getConnection().createChannel();
				channelReferenceSet.add(new WeakReference<Channel>(channel));
			}
		} catch (IOException e) {
			logger.error("Error createChannel", e);
		} catch (Exception e) {
			logger.error("Error createChannel", e);
		}
		return channel;
	}

	public void destroy() throws Exception {
		closeChannels();
	}

	/**
	 * 销毁所有channels
	 */
	private void closeChannels() {
		for (Reference<Channel> channelReference : channelReferenceSet) {
			try {
				Channel channel = channelReference.get();
				if (channel != null && channel.isOpen()) {
					if (channel.getConnection().isOpen()) {
						channel.close();
					}
				}
			} catch (NullPointerException e) {
				logger.error("Error closing channel", e);
			} catch (IOException e) {
				logger.error("Error closing channel", e);
			} catch (TimeoutException e) {
				logger.error("Error closing channel", e);
			}
		}
		channelReferenceSet.clear();
	}

}
