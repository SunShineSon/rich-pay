package com.service.mq.rabbitmq.template;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hlb.service.mq.rabbitmq.model.MessageModel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import com.service.mq.rabbitmq.channel.RabbitMQChannelFactory;
import com.service.mq.rabbitmq.constant.RabbitMQConstant;
import com.service.mq.rabbitmq.enums.ExchangeTypeEnum;
import com.service.mq.rabbitmq.enums.RoutingKeyEnum;

public class RabbitMQTemplate implements InitializingBean, ShutdownListener, ReturnListener, ConfirmListener {

	private static Logger logger = Logger.getLogger(RabbitMQTemplate.class);
	
	private RabbitMQChannelFactory rabbitMQChannelFactory;
	
	private String exchange;
	private String exchangeType = ExchangeTypeEnum.DIRECT.getType();
	private String routingKey = RoutingKeyEnum.DEFAULT_ROUTINGKEY.getName();
	private String queueName;
    private boolean mandatory = true;
    private boolean immediate = false;
    private boolean confirmSelect = false;

	private Channel channel;

    public void send(Serializable object) {
        send(object, routingKey, mandatory, immediate);
    }

    public void send(Serializable object, String routingKey) {
        send(object, routingKey, mandatory, immediate);
    }

    public void send(Serializable object, boolean mandatory, boolean immediate) {
        send(object, routingKey, mandatory, immediate);
    }

    public void send(Serializable object, String routingKey, boolean mandatory, boolean immediate) {
    	
    	Assert.notNull(object, "消息对象不能为空");
    	
        try {
        	
        	//构造消息模型
        	MessageModel model = new MessageModel(String.valueOf(channel.getNextPublishSeqNo()),object);
        	
        	//exchange：交换器名称
        	//routingKey：路由键
        	//mandatory：服务器确认能找到queue,否则将消息返还给生产者
        	//immediate：服务器确认queue上有绑定消费者,否则将消息返还给生产者
        	//PERSISTENT_TEXT_PLAIN：将消息设置为可持久化
        	//model：发送的消息，需要序列化
            channel.basicPublish(exchange, routingKey, mandatory, immediate, MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize(model));
            
        } catch (Exception e) {
        	logger.error(e,e.getCause());
            throw new RuntimeException(e);
        }
    }
    
	/** 
	 * Acks represent messages handled successfully
	 * rabbitmq的消息确认机制。当消费端回ack后，生产端进行确认
	 */
	public void handleAck(long deliveryTag, boolean multiple) throws IOException {
		logger.info("ack, deliveryTag: " + deliveryTag + ", multiple: " + multiple);
	}

	/** 
	 * Nacks represent messages lost by the broker. 
	 * Note, the lost messages could still have been delivered to consumers, 
	 * but the broker cannot guarantee this
	 */
	public void handleNack(long deliveryTag, boolean multiple) throws IOException {
		logger.error("Nack, deliveryTag: " + deliveryTag + ", multiple: " + multiple);
	}

	/**
	 * In order to be notified of failed deliveries 
	 * when basicPublish is called with "mandatory" or "immediate" flags set.
	 *
	 */
	public void handleReturn(
			int replyCode,
            String replyText,
            String exchange,
            String routingKey,
            AMQP.BasicProperties properties,
            byte[] body) throws IOException {
		
		logger.error("exchange无匹配的队列。replyCode：" + replyCode + ",replyText：" + replyText
				 + ",exchange：" + exchange + ",routingKey：" + routingKey
				 + ",properties：" + properties + ",body：" + body);
		
	}

	/** 
	 * A ShutdownListener receives information about the shutdown of connections and channels.
	 * Note that when a connection is shut down, 
	 * its associated channels are also considered shut down and their ShutdownListeners will be notified (with the same cause). 
	 * Because of this, and the fact that channel ShutdownListeners execute in the connection's thread, 
	 * attempting to make blocking calls on a connection inside the listener will lead to deadlock.
	 * 
	 */
	public void shutdownCompleted(
			ShutdownSignalException shutdownsignalexception) {
		logger.info("connection is shut down");
		if(shutdownsignalexception != null ){
			logger.error(shutdownsignalexception.getMessage());
		}
	}

	public void afterPropertiesSet() throws Exception {
		connectChannel();
	}
	
	/**
	 * 创建生产者的channel
	 *
	 */
	private void connectChannel() {
        if (channel == null || !channel.isOpen()) {
            try {
            	//创建生产者的channel
                channel = rabbitMQChannelFactory.createChannel();
                
                if(channel != null && channel.getConnection() != null){
                	//添加channel连接的停止监听
                    channel.getConnection().addShutdownListener(this);
                    
        			//增加return监听器，当发布消息但无匹配的队列时消息被返回
        			channel.addReturnListener(this);
        			//添加channel的停止监听
        			channel.addShutdownListener(this);
        			//生产者交换机定义
                    channel.exchangeDeclare(exchange, exchangeType, RabbitMQConstant.getDurable());
                    
                    //如果配置了queueName和routingKey，则定义和绑定队列
                    if (StringUtils.isNotBlank(queueName) && StringUtils.isNotBlank(routingKey)) {
                    	channel.queueDeclare(queueName, RabbitMQConstant.getDurable(),
            		    		RabbitMQConstant.getExclusive(), RabbitMQConstant.getAutoDelete(), null);
                    	channel.queueBind(queueName, exchange, routingKey);
    				}
                    
                    //ACK机制
                    if(confirmSelect){
                    	//消息确认机制。添加channel的确认监听
            			channel.addConfirmListener(this);
                    	//消息确认机制
                        channel.confirmSelect();
                    }
                }
            } catch (IOException e) {
            	logger.error("连接channel失败！", e);
            }
        }
    }
	
	
    @Required
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Required
    public void setChannelFactory(RabbitMQChannelFactory rabbitMQChannelFactory) {
        this.rabbitMQChannelFactory = rabbitMQChannelFactory;
    }
    
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }
    
    public void setConfirmSelect(boolean confirmSelect) {
		this.confirmSelect = confirmSelect;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
}
