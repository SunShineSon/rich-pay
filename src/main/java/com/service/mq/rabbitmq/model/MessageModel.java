package com.service.mq.rabbitmq.model;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Required;

/**
 * 消息模板
 *
 */
public class MessageModel implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一序列号
	 *
	 */
	private String seqNo;
	
	/**
	 * 消息对象
	 *
	 */
	private Serializable object;
	
	public MessageModel(String seqNo, Serializable object) {  
        this.seqNo = seqNo;  
        this.object = object;
    }
	
	public Serializable getObject() {
		return object;
	}

	public void setObject(Serializable object) {
		this.object = object;
	}

	public String getSeqNo() {
		return seqNo;
	}

	@Required
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	
	@Override
	public String toString() {
		return "MessageModel [seqNo=" + seqNo + ", object=" + object + "]";
	}
}
