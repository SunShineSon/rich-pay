package com.service.sms.util;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import net.sf.json.JSONObject;

/**
 * 
 * 2018年5月14日 下午4:39:05
 * 
 * @Author：郭
 * @Description： 
 *
 */
public class SmsAliyunUtil {

	private static Log log = LogFactory.getLog(SmsAliyunUtil.class);
	private static ClientContext context = ClientContext.getInstance().init();
	private static SubShortResult result;
	private static String[] constant = {};
	
	
	private static IAcsClient getAscClient() {
		// 初始化ascClient
		IClientProfile profile = DefaultProfile.getProfile(context.getTopic(), context.getAccessId(),
				context.getAccessKey());
		try {
			DefaultProfile.addEndpoint(context.getTopic(), context.getTopic(), context.getMessageBody(), context.getMNSEndpoint());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new DefaultAcsClient(profile);
	}
	
	public static SubShortResult sendMessage(String mobile, String[] vars, String projectId) {
		try {
			// 组装请求对象
			SendSmsRequest request = new SendSmsRequest();
			// 使用post提交
			request.setMethod(MethodType.POST);
			// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
			request.setPhoneNumbers(mobile);
			// 必填:短信签名-可在短信控制台中找到
			request.setSignName(context.getSignName());
			// 必填:短信模板-可在短信控制台中找到
			request.setTemplateCode(projectId);
			// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
			// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
			// request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
			constant = context.getConstant().split(",");
			Map<String, String> map = new HashMap<String, String>();
			for (int i=0; i<vars.length; i++) {
				map.put(constant[i], vars[i]);
			}
			JSONObject jsonObject = JSONObject.fromObject(map); 
			log.info(jsonObject.toString());
			
			request.setTemplateParam(jsonObject.toString());
			// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
			// request.setSmsUpExtendCode("90997");
			// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
			// request.setOutId("yourOutId");
			// 请求失败这里会抛ClientException异常
			SendSmsResponse sendSmsResponse = getAscClient().getAcsResponse(request);
			result = new SubShortResult();
			if (sendSmsResponse.getCode() == null || !sendSmsResponse.getCode().equals("OK")) {
				result.setMsg("发送失败");
				result.setStatus(1);
				return result;
			}
			result.setMsg("发送成功");
			result.setStatus(0);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("发送失败");
			result.setStatus(1);
			log.warn("发送失败，to=" + mobile + ",content=" + vars + ",projectId=" + projectId + "result=1,"
					+ e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}

