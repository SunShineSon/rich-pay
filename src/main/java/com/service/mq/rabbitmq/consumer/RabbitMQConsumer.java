package com.service.mq.rabbitmq.consumer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.MethodInvoker;
import org.springframework.util.ObjectUtils;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ShutdownSignalException;
import com.service.mq.rabbitmq.channel.RabbitMQChannelFactory;
import com.service.mq.rabbitmq.constant.RabbitMQConstant;
import com.service.mq.rabbitmq.enums.ExchangeTypeEnum;
import com.service.mq.rabbitmq.enums.RoutingKeyEnum;
import com.service.mq.rabbitmq.model.MessageModel;

/**
 * RabbitMQ消费者类
 *
 */
public class RabbitMQConsumer implements Consumer, InitializingBean {

	private static Logger logger = Logger.getLogger(RabbitMQConsumer.class);
	
    private RabbitMQChannelFactory rabbitMQChannelFactory;
	
	private Object delegate;
	private String exchange;
    private String exchangeType = ExchangeTypeEnum.DIRECT.getType();
    private String queueName;
    private String routingKey = RoutingKeyEnum.DEFAULT_ROUTINGKEY.getName();
    private String method = "handleMessage";
    private Channel channel;
    
    /**
	 * 已消费的序列号（防止网络重连后的重复消费）
	 *
	 */
    private List<String> consumedSeqNos = new ArrayList<String>();

    /**
	 * 初始化bean后执行的方法
	 */
	public void afterPropertiesSet() throws Exception {
		consume();
	}

	/**
	 * 消费端注册到Rabbtmq服务端
	 *
	 */
	private void consume() {
		if (channel == null || !channel.isOpen()) {
            try {
            	//创建消费端channel
                channel = rabbitMQChannelFactory.createChannel();
                
                if(channel != null){
                	String realQueueName;
                    if (queueName == null) {
                        // 没有定义对列名称则取匿名队列名称
                    	realQueueName = channel.queueDeclare().getQueue();
                    } else {
                    	//queueName:队列名
                    	//durable:队列是否可持久化
                    	//exclusive:当消费者断开时，是否删除队列
                    	//autoDelete:当绑定的队列都不使用时，是否自动删除交换机
                    	realQueueName = channel.queueDeclare(queueName, RabbitMQConstant.getDurable(),
            		    		RabbitMQConstant.getExclusive(), RabbitMQConstant.getAutoDelete(), null).getQueue();
                    }
                    //定义交换机名称、类型，并设置交换机可持久化
                    channel.exchangeDeclare(exchange, exchangeType, RabbitMQConstant.getDurable());
                    //根据路由键（routingKey），绑定队列与交换机
                    channel.queueBind(realQueueName, exchange, routingKey);
                    //调度分发消息的方式。当消费端处理完消息并确认完后，才发送下一跳消息。
                    channel.basicQos(1);
                    //定义消费行为
                    boolean autoAck = false;
                    channel.basicConsume(realQueueName, autoAck, this);
                }                
            } catch (IOException e) {
            	logger.error("启动消费端失败", e);
            }
        }
	}
	
	/**
	 * Called when the consumer is cancelled for reasons other than by a call to Channel.basicCancel(java.lang.String). 
	 * For example, the queue has been deleted. See handleCancelOk(java.lang.String) for notification of consumer 
	 * cancellation due to Channel.basicCancel(java.lang.String).
	 */
	public void handleCancel(String consumerTag) throws IOException {
		logger.info("Consumer handleCancel. consumerTag:" + consumerTag);
	}

	/**
	 * Called when the consumer is cancelled by a call to Channel.basicCancel(java.lang.String).
	 */
	public void handleCancelOk(String consumerTag) {
		logger.info("Consumer handleCancelOk. consumerTag:" + consumerTag);
	}

	/**
	 * Called when the consumer is registered by a call to any of 
	 * the Channel.basicConsume(java.lang.String, com.rabbitmq.client.Consumer) methods.
	 */
	public void handleConsumeOk(String consumerTag) {
		logger.info("Consumer handleConsumeOk. consumerTag:" + consumerTag);
	}

	/**
	 * Called when a basic.deliver is received for this consumer.
	 * 
	 * @param consumerTag the consumer tag associated with the consumer
	 * @param envelope packaging data for the message
	 * @param properties content header data for the message
	 * @param body the message body (opaque, client-specific byte array)
	 *
	 */
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
		Object message = SerializationUtils.deserialize(body);
		if(message != null && message instanceof MessageModel){
			MessageModel messageModel = (MessageModel)message;
			boolean invokeResult = true;
			try {
				//防止网络重连后的重复消费
				if(!consumedSeqNos.contains(messageModel.getSeqNo())){
					Object result = invokeMethod(method, new Object[]{messageModel.getObject()});
					
		            if (result != null && result instanceof Serializable) {
		                channel.basicPublish(envelope.getExchange(), properties.getReplyTo(), 
		                		MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize((Serializable) result));
		            }
		            consumedSeqNos.add(messageModel.getSeqNo());
				}
			} catch (Exception ex) {
				logger.error("消费消息出错！" + "delegate:" + delegate + ", "
						+ "method:" + method + ", message:" + message.toString() + ", error:" + ex, ex);
				//basicNack可以将失败的消息重新加载到队列，消息重新被发送到下一个消费者消费。如果只绑定一个消费者，将会重复消费。
				//channel.basicNack(envelope.getDeliveryTag(), false, true);
				invokeResult = false;
			}
			
			//执行结果正常，回ack到生产者
			if(invokeResult){
				channel.basicAck(envelope.getDeliveryTag(), false);
				consumedSeqNos.remove(messageModel.getSeqNo());
			}
			
		} else {
			logger.error("请封装为MessageModel类型");
		}
	}

	protected Object invokeMethod(String methodName, Object[] arguments) throws Exception{
        try {
            MethodInvoker methodInvoker = new MethodInvoker();
            methodInvoker.setTargetObject(delegate);
            methodInvoker.setTargetMethod(methodName);
            methodInvoker.setArguments(arguments);
            methodInvoker.prepare();
            return methodInvoker.invoke();
        } catch (InvocationTargetException ex) {
        	logger.error("delegate: " + delegate + " invokeMethod " + methodName + " with arguments " +
            		ObjectUtils.nullSafeToString(arguments) + " threw exception：" + ex.getTargetException());
			return null;
        } catch (Throwable ex) {
            logger.error("delegate: " + delegate + " invokeMethod " + methodName + " with arguments " +
            		ObjectUtils.nullSafeToString(arguments) + " threw exception：" + ex.getMessage(),ex);
			return null;
        }
    }
	
	 /**
     * Called when a basic.recover-ok is received
     * in reply to a basic.recover. All messages
     * received before this is invoked that haven't been ack'ed will be
     * re-delivered. All messages received afterwards won't be.
     * 
     * @param consumerTag the consumer tag associated with the consumer
     */
	public void handleRecoverOk(String consumerTag) {
		logger.info("Consumer handleRecoverOk. consumerTag:" + consumerTag);
	}
	
	/**
     * Called when either the channel or the underlying connection has been shut down.
	 *
     * @param consumerTag the consumer tag associated with the consumer
     * @param
     */
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException cause) {
		logger.info("Consumer handleShutdownSignal. consumerTag:" + consumerTag);
		
		if(cause.getReason() != null){
			logger.info("Consumer ShutdownSignalException. reason:" + cause.getReason());
		}else if(cause.isHardError()){
			logger.error("Consumer handleShutdownSignal isHardError.");
		}
	}

    @Required
    public void setChannelFactory(RabbitMQChannelFactory rabbitMQChannelFactory) {
        this.rabbitMQChannelFactory = rabbitMQChannelFactory;
    }

    @Required
	public void setDelegate(Object delegate) {
		this.delegate = delegate;
	}

    @Required
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	@Required
	public void setMethod(String method) {
		this.method = method;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
