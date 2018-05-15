package com.service.sms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientContext {

	private static final Logger log = LoggerFactory
			.getLogger(ClientContext.class);
	/*
	 * Properties
	 */
	private static final String API_PROTOCAL_KEY = "API_PROTOCAL";

	private static final String API_HOST_KEY = "API_HOST";

	private static final String API_ORG_KEY = "API_ORG";

	private static final String APP_CLIENT_KEY_KEY = "APP_CLIENT_KEY";

	private static final String APP_CLIENT_SECRET_KEY = "APP_CLIENT_SECRET";

	private static final String APP_CLIENT_NONCE_KEY = "APP_CLIENT_NONCE";

	private static final String SMS_ACCOUNT = "SMS_ACCOUNT";
	private static final String SMS_PASSWORD = "SMS_PASSWORD";
	private static final String CMHOST_IP = "CMHOST_IP";
	private static final String WSHOST_IP = "WSHOST_IP";
	private static final String CMHOST_PORT = "CMHOST_PORT";
	private static final String WSHOST_PORT = "WSHOST_PORT";
	
	private static final String ACCESSID = "ACCESSID";
	private static final String ACCESSKEY = "ACCESSKEY";
	private static final String MNSENDPOINT = "MNSENDPOINT";
	private static final String TOPIC = "TOPIC";
	private static final String SINGNAME = "SINGNAME";
	private static final String MESSAGEBODY = "MESSAGEBODY";
	private static final String CONSTANT = "CONSTANT";

	private String sms_account;
	private String sms_password;
	private String cmhost_ip;
	private String wshost_ip;
	private String cmhost_port;
	private String wshost_port;
	
	private String accessId;
	private String accessKey;
	private String MNSEndpoint;
	private String topic;
	private String signName;
	private String messageBody;
	private String constant;
	
	
	private static ClientContext context;

	private Boolean initialized = Boolean.FALSE;

	private String protocal;

	private String host;

	private String org;

	private String clientKey;

	private String clientSecret;

	private String nonce;

	private String curTime;

	private String checkSun;

	private NeteaseRestAPIFactory factory;

	private ClientContext() {
	};

	public static ClientContext getInstance() {
		if (null == context) {
			context = new ClientContext();
		}

		return context;
	}

	public ClientContext init() {
		if (initialized) {
			log.warn("Context has been initialized already, skipped!");
			return context;
		} else {
			initFromPropertiesFile();
			initFromPropertiesFile("conf/message-config.properties" );
			initialized = Boolean.TRUE;
		}
		return context;
	}

	public NeteaseRestAPIFactory getAPIFactory() {
		if (!context.isInitialized()) {
			log.error(MessageTemplate.INVAILID_CONTEXT_MSG);
			throw new RuntimeException(MessageTemplate.INVAILID_CONTEXT_MSG);
		}

		if (null == this.factory) {
			this.factory = NeteaseRestAPIFactory.getInstance(context);
		}
		return this.factory;
	}

	public String getSeriveURL() {
		if (null == context || !context.isInitialized()) {
			log.error(MessageTemplate.INVAILID_CONTEXT_MSG);
			throw new RuntimeException(MessageTemplate.INVAILID_CONTEXT_MSG);
		}

		String serviceURL = context.getProtocal() + "://" + context.getHost()
				+ "/" + context.getOrg();

		return serviceURL;
	}

	private void initFromPropertiesFile() {
		Properties p = new Properties();

		try {
			InputStream inputStream = ClientContext.class.getClassLoader()
					.getResourceAsStream("conf/netease-config.properties");
			p.load(inputStream);
		} catch (IOException e) {
			log.error(MessageTemplate.print(MessageTemplate.FILE_ACCESS_MSG,
					new String[] { "conf/netease-config.properties" }));
			return; // Context not initialized
		}

		String protocal = p.getProperty(API_PROTOCAL_KEY);
		String host = p.getProperty(API_HOST_KEY);
		String org = p.getProperty(API_ORG_KEY);
		String clientKey = p.getProperty(APP_CLIENT_KEY_KEY);
		String clientSecret = p.getProperty(APP_CLIENT_SECRET_KEY);
		String nonce = p.getProperty(APP_CLIENT_NONCE_KEY);
		String curTime = String.valueOf((new Date()).getTime() / 1000L);

		if (StringUtils.isBlank(protocal) || StringUtils.isBlank(host)
				|| StringUtils.isBlank(org) || StringUtils.isBlank(clientKey)
				|| StringUtils.isBlank(clientSecret)
				|| StringUtils.isBlank(nonce)) {
			log.error(MessageTemplate.print(
					MessageTemplate.INVAILID_PROPERTIES_MSG,
					new String[] { "conf/netease-config.properties" }));
			return; // Context not initialized
		}

		context.protocal = protocal;
		context.host = host;
		context.org = org;
		context.clientKey = clientKey;
		context.clientSecret = clientSecret;
		context.nonce = nonce;
		 
		context.curTime = curTime;
		context.checkSun = CheckSumBuilder.getCheckSum(context.clientSecret,nonce, context.curTime);

		log.debug("protocal: " + context.protocal);
		log.debug("host: " + context.host);
		log.debug("org: " + context.org);
		log.debug("clientKey: " + context.clientKey);
		log.debug("clientSecret: " + context.clientSecret);
		log.debug("curTime: " + context.clientKey);
		log.debug("checkSun: " + context.checkSun);
	}
	
	private void initFromPropertiesFile(String path) {
		Properties p = new Properties();

		try {
			InputStream inputStream = ClientContext.class.getClassLoader()
					.getResourceAsStream(path);
			p.load(inputStream);
		} catch (IOException e) {
			log.error(MessageTemplate.print(MessageTemplate.FILE_ACCESS_MSG,
					new String[] { path}));
			return; // Context not initialized
		}

		String sms_account = p.getProperty(SMS_ACCOUNT);
		String sms_password = p.getProperty(SMS_PASSWORD);
		String cmhost_ip = p.getProperty(CMHOST_IP);
		String cmhost_port = p.getProperty(CMHOST_PORT);
		String wshost_ip = p.getProperty(WSHOST_IP);
		String wshost_port = p.getProperty(WSHOST_PORT);
		
		String accessId = p.getProperty("ACCESSID");
		String accessKey = p.getProperty("ACCESSKEY");
		String MNSEndpoint = p.getProperty("MNSENDPOINT");
		String topic = p.getProperty("TOPIC");
		String signName = p.getProperty("SINGNAME");
		String messageBody = p.getProperty("MESSAGEBODY");
		String constant = p.getProperty("CONSTANT");
		
		
		if (StringUtils.isBlank(accessId) || StringUtils.isBlank(accessKey)
				|| StringUtils.isBlank(MNSEndpoint) || StringUtils.isBlank(topic)
				|| StringUtils.isBlank(signName)
				|| StringUtils.isBlank(messageBody)
				|| StringUtils.isBlank(constant)) {
			log.error(MessageTemplate.print(
					MessageTemplate.INVAILID_PROPERTIES_MSG,
					new String[] { "conf/message-config.properties" }));
			return;
		}

		if (StringUtils.isBlank(sms_account) || StringUtils.isBlank(sms_password)
				|| StringUtils.isBlank(cmhost_ip) || StringUtils.isBlank(cmhost_port)
				|| StringUtils.isBlank(wshost_ip)
				|| StringUtils.isBlank(wshost_port)) {
			log.error(MessageTemplate.print(
					MessageTemplate.INVAILID_PROPERTIES_MSG,
					new String[] { "conf/message-config.properties" }));
			return;
		}
		//XW
		context.sms_account = p.getProperty(this.SMS_ACCOUNT).trim();
		context.sms_password = p.getProperty(this.SMS_PASSWORD).trim();
		context.cmhost_ip = p.getProperty(this.CMHOST_IP).trim();
		context.cmhost_port = p.getProperty(this.CMHOST_PORT).trim();
		context.wshost_ip = p.getProperty(this.WSHOST_IP).trim();
		context.wshost_port = p.getProperty(this.WSHOST_PORT).trim();
		//aliyun
		context.accessId = p.getProperty(this.ACCESSID).trim();
		context.accessKey = p.getProperty(this.ACCESSKEY).trim();
		context.MNSEndpoint = p.getProperty(this.MNSENDPOINT).trim();
		context.topic = p.getProperty(this.TOPIC).trim();
		context.signName = p.getProperty(this.SINGNAME).trim();
		context.messageBody = p.getProperty(this.MESSAGEBODY).trim();
		context.constant = p.getProperty(this.CONSTANT).trim();
 
 
		log.debug("accessId: " + context.accessId);
		log.debug("accessKey:" + context.accessKey);
		log.debug("MNSEndpoint:" + context.MNSEndpoint);
		log.debug("topic:" + context.topic);
		log.debug("signName:" + context.signName);
		log.debug("messageBody:" + context.messageBody);
		log.debug("constant:" + context.constant);
	}

	public String getProtocal() {
		return protocal;
	}

	public String getHost() {
		return host;
	}

	public String getOrg() {
		return org;
	}

	public String getClientKey() {
		return clientKey;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getNonce() {
		return nonce;
	}

	public String getCurTime() {
		return curTime;
	}

	public String getCheckSun() {
		return checkSun;
	}

	public Boolean isInitialized() {
		return initialized;
	}

	public String getSms_account() {
		return sms_account;
	}

	public String getSms_password() {
		return sms_password;
	}

	public String getCmhost_ip() {
		return cmhost_ip;
	}

	public String getWshost_ip() {
		return wshost_ip;
	}

	public String getCmhost_port() {
		return cmhost_port;
	}

	public String getWshost_port() {
		return wshost_port;
	}

	public String getAccessId() {
		return accessId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getMNSEndpoint() {
		return MNSEndpoint;
	}

	public String getTopic() {
		return topic;
	}

	public String getSignName() {
		return signName;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public String getConstant() {
		return constant;
	}

}
