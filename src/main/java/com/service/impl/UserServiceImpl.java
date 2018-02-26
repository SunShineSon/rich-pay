package com.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.common.api.annotion.RedisCache;
import com.common.api.constants.RedisConstants;
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
	
	@RedisCache(db = RedisConstants.USER, key = "queryUserById_${id}", params = "id", seconds = 60 * 10, override = true)
	public User queryUserById(Long id){
		log.info("db select！");
		return null;
		
	}
	
	@RedisCache(db = RedisConstants.USER, key = "argsParam_${id}_${name}_${type}", params = "name,id,type", seconds = 60 * 10, override = true)
	public void argsParam(String name, Long id, int type){
		
		log.info("argsParams info....");
	}

}
