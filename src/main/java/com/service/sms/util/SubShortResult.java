package com.service.sms.util;

public class SubShortResult {
	
	private int status;
	
	private int code;
	
	private String msg;
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean isSuccess(){
		return 0==this.getStatus();
	}
	
	@Override
	public String toString() {
		return "[status:"+status+", code:"+code+", msg:"+msg+"]";
	}

}
