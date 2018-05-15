package com.service.sms.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NeteaseRestAPIFactory {
	
	public static final String USER_CLASS = "NeteaseIMUsers";
	
	public static final String FRIEND_CLASS = "NeteaseIMFriend";
	
	public static final String HISTORY_CLASS = "NeteaseIMHistory";
	
	public static final String MESSAGE_CLASS = "EasemobChatMessage";
	
	public static final String SEND_MESSAGE_CLASS = "EasemobSendMessage";
	
	public static final String CHATGROUP_CLASS = "EasemobChatGroup";
	
	public static final String CHATROOM_CLASS = "EasemobChatRoom";
	
	private static final String BASE_PACKAGE = "com.hlb.spat.netease.api.impl";
	
	private static final String METHOD_SET_CONTEXT = "setContext";
	
	private static final String METHOD_SET_INVOKER = "setInvoker";
	
	private static final Logger log = LoggerFactory.getLogger(NeteaseRestAPIFactory.class);
	
	private static NeteaseRestAPIFactory factory;
	
	private ClientContext context;
	
	private HttpClientRestAPIInvoker httpclient = new HttpClientRestAPIInvoker();

	private NeteaseRestAPIFactory(ClientContext context) {
		this.context = context;
	}
	
	public static NeteaseRestAPIFactory getInstance(ClientContext context) {
		if( null == factory ) {
			if( null == context || !context.isInitialized() ) {
				log.warn(MessageTemplate.INVAILID_CONTEXT_MSG);
				log.warn(MessageTemplate.AUTOMATIC_CONTEXT_MSG);
				context = ClientContext.getInstance().init();
				
				if( !context.isInitialized() ) {
					log.error(MessageTemplate.INVAILID_CONTEXT_MSG);
					throw new RuntimeException(MessageTemplate.INVAILID_CONTEXT_MSG);
				}
			}
			
			factory = new NeteaseRestAPIFactory(context);
		}
		
		return factory;
	}
	
	public NeteaseRestAPI newInstance(String className) {
		return (NeteaseRestAPI)getClassInstance(className);
	}
	
	private Object getClassInstance(String className) {
		Class<?> targetClass = null;
		Object newObj = null;
		
		try {
			targetClass = Class.forName(BASE_PACKAGE + "." + className);
			newObj = targetClass.newInstance();
		} catch (Exception e) {
			String msg = MessageTemplate.print(MessageTemplate.ERROR_CLASS_MSG, new String[]{className});
			log.error(msg, e); 
			throw new RuntimeException(msg);
		}
		
		// Inject the context and invoker, they are defined in EasemobRestAPI
		try {
			targetClass.getMethod(METHOD_SET_CONTEXT, ClientContext.class).invoke(newObj, this.context);
		} catch (Exception e) {
			String msg = MessageTemplate.print(MessageTemplate.ERROR_METHOD_MSG, new String[]{METHOD_SET_CONTEXT});
			log.error(msg, e);
			throw new RuntimeException(msg);
		}
		
		try {
			targetClass.getMethod(METHOD_SET_INVOKER, RestAPIInvoker.class).invoke(newObj, httpclient);
		} catch (Exception e) {
			String msg = MessageTemplate.print(MessageTemplate.ERROR_METHOD_MSG, new String[]{METHOD_SET_INVOKER});
			log.error(msg, e);
			throw new RuntimeException(msg);
		}
		
		return newObj;
	}

	public ClientContext getContext() {
		return context;
	}
	
}
