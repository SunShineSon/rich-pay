package com.service.sms.entity;


import java.util.ArrayList;
import java.util.List;

public class ResponseWrapper {
	private static final String WARNING = "[WARNING]: ";
	private static final String ERROR = "[ERROR]: ";

	private List<String> messages = new ArrayList<String>();
	private Integer responseStatus;
	private Object responseEntity;
	private Boolean hasError = Boolean.FALSE;

	public static ResponseWrapper newInstance() {
		return new ResponseWrapper();
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addWarning(String warning) {
		messages.add(WARNING + warning);
	}

	public void addError(String error) {
		messages.add(ERROR + error);
		hasError = Boolean.TRUE;
	}

	public Boolean hasError() {
		return hasError;
	}

	public Object getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(Object responseEntity) {
		this.responseEntity = responseEntity;
	}

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String message : messages) {
			sb.append(message).append("\n");
		}
		sb.append("Status: ").append(responseStatus).append("\n");
		if (null != responseEntity) {
			if (responseEntity.getClass().getName().endsWith("ObjectNode")) {
				sb.append("Response Entity: ")
						.append(responseEntity.toString());
			} else {
				sb.append("Response Entity: ").append(
						responseEntity.getClass().getName());
			}
		}
		return sb.toString();
	}
}
