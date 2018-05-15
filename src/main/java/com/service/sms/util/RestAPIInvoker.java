package com.service.sms.util;

import com.service.sms.entity.EntityWrapper;
import com.service.sms.entity.HeaderWrapper;
import com.service.sms.entity.ResponseWrapper;


public interface RestAPIInvoker {
	ResponseWrapper sendRequest(String method, String url,
			HeaderWrapper header, EntityWrapper body);
}
