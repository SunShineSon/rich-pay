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
}
