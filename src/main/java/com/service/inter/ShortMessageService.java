package com.service.inter;

import com.service.sms.util.SubShortResult;

public interface ShortMessageService {

	SubShortResult sendMessage(String mobile, String[] vars, String projectId);
}
