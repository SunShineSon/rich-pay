package com.service.inter;

import com.entity.user.User;

/**
 * 
 * 2018年2月11日 上午11:57:39
 * 
 * @Author：郭
 * @Description： 
 *
 */
public interface UserService {

	/**
	 * 根据ID查询用户数据
	 * @param id
	 * @return
	 */
	User queryUserById(Long id);
	
	
	void argsParam(String name, Long id, int type);

	/**
	 * 新增用户
	 * 2018年2月28日 下午3:44:27
	 * @Author：郭
	 * @param user
	 * @Description：
	 */
	int addUser(User user);

}
