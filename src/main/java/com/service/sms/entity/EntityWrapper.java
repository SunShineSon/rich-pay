package com.service.sms.entity;

import java.util.List;
import org.apache.http.NameValuePair;
public interface EntityWrapper {
	List<NameValuePair> getEntity();
	Boolean validate();
}
