package com.entity.user;

import java.io.Serializable;

/**
 * 
 * 2018年2月11日 上午9:45:07
 * 
 * @Author：郭
 * @Description： 
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String id ;
	
	private String name;
	
	private int age;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ",name=" + name + ",age=" + age + "]";
	}
}

