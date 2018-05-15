package com.service.sms.util;


public interface RestAPI {
	String getResourceRootURI(String uri);

	String getResourceRootURL();

	String getResourceRootPREFIX();
	
	String getIMUserNamePREFIX();
}
