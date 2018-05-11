package com.jpush;

import java.util.List;

import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送工具类 https://github.com/
 * 
 * @author
 *
 */
public class JPushUtil {
	/**
	 * 所有平台，所有设备，内容为 content 的通知
	 * 
	 * @param content
	 * @return
	 */
	public static PushPayload buildPushObject_all_all_alert(String content) {
		return PushPayload.alertAll(content);
	}

	/**
	 * 根据 设备终端ID 推送消息
	 * 
	 * @param regesterIds
	 *            设备终端ID集合
	 * @param content
	 *            内容
	 * @return
	 */
	public static PushPayload buildPushObject_all_all_regesterIds(List<String> regesterIds, String content) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(regesterIds))
				.setNotification(Notification.alert(content)).build();

	}

	/**
	 * 所有平台，推送目标是别名为 "alias"，通知内容为 TEST
	 * 
	 * @param alias
	 * @param content
	 * @return
	 */
	public static PushPayload buildPushObject_all_alias_alert(String alias, String content) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(content)).build();
	}

}
