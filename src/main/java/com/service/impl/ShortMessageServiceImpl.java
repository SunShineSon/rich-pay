package com.service.impl;

import org.springframework.stereotype.Service;

import com.service.inter.ShortMessageService;
import com.service.sms.util.SmsAliyunUtil;
import com.service.sms.util.SubShortResult;

@Service
public class ShortMessageServiceImpl implements ShortMessageService {
	
	public SubShortResult sendMessage(String mobile, String[] vars, String projectId) {
		SubShortResult res = SmsAliyunUtil.sendMessage(mobile, vars, projectId);
		return res;
	}

}

