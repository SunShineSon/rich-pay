package com.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.common.api.annotion.RedisCache;
import com.common.api.constants.RedisConstants;
import com.dao.user.UserMapper;
import com.entity.user.User;
import com.service.inter.UserService;

/**
 * 
 * 2018年2月11日 上午11:57:46
 * 
 * @Author：郭
 * @Description： 
 *
 */
@Component
@Service
public class UserServiceImpl implements UserService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserMapper userMapper;
	
	@RedisCache(db = RedisConstants.USER, key = "queryUserById_${id}", params = "id", seconds = 60 * 10, override = true)
	public User queryUserById(Long id){
		log.info("db select！");
		return null;
		
	}
	
	@RedisCache(db = RedisConstants.USER, key = "argsParam_${id}_${name}_${type}", params = "id,name,type", seconds = 60 * 10, override = true)
	public void argsParam(String name, Long id, int type){
		
		log.info("argsParams info....");
	}

	/**
	 * 新增用户
	 * 2018年2月28日 下午3:37:38
	 * @Author 郭
	 * @see com.service.inter.UserService#addUser(com.entity.user.User)
	 * @Description:
	 */
	public int addUser(User user){
		return userMapper.insert(user);
	}
}
