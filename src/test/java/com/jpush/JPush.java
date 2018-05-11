package com.jpush;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;

/**
 * 
 * 2018年4月25日 上午11:48:25
 * 
 * @Author：郭
 * @Description： 
 *
 */
public class JPush {
	
	private final static Logger LOG = LoggerFactory.getLogger(JPush.class);
	

	public static void main(String[] args) {
	}
	
    public static void testSendWithSMS() {
        JPushClient jpushClient = new JPushClient("masterSecret", "appKey");
        try {
            SMS sms = SMS.content("Test SMS", 10);
            PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "test sms", sms, "alias1");
            LOG.info("Got result - " + result);
        } catch (Exception e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
    }

}

