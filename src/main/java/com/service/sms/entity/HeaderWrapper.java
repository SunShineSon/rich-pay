package com.service.sms.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class HeaderWrapper {

	private List<NameValuePair> headers = new ArrayList<NameValuePair>();

	public static HeaderWrapper newInstance() {
		return new HeaderWrapper();
	}

	public HeaderWrapper addHeader(String key, String value) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
			return this;
		}

		headers.add(new BasicNameValuePair(key, value));
		return this;
	}

	public HeaderWrapper addUrlenCodedContentHeader(String name, String value) {
		return addHeader(name, value);
	}

	public List<NameValuePair> getHeaders() {
		return headers;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (NameValuePair header : headers) {
			sb.append("[").append(header.getName()).append(":")
					.append(header.getValue()).append("] ");
		}

		return sb.toString();
	}
}
