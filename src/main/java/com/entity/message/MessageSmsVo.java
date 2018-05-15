package com.entity.message;


import java.io.Serializable;
import java.util.Arrays;

/**
 * mq传递参数对象
 * @author : 郭
 * @Time ： 2018年5月11日 下午2:37:22
 * @Desciption：
 */
public class MessageSmsVo implements Serializable{

	private static final long serialVersionUID = 1L;

	// 接收人手机号
	private String phone;
	
	// 短信模板ID(阿里账户上短信模板ID)
	private String projectId;
	
	//参数值
	private String[] args;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "MessageSmsVo [phone=" + phone + ", projectId=" + projectId + ", args=" + Arrays.toString(args) + "]";
	}
	
}
