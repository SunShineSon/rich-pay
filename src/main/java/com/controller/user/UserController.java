package com.controller.user;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.util.UUid;
import com.entity.user.User;
import com.service.inter.UserService;

/**
 * 用户接口
 * 2018年2月11日 下午2:54:31
 * 
 * @Author：郭
 * @Description： 
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	/**
	 * 
	 * 2018年2月11日 下午12:02:40
	 * @Author：郭
	 * @param req
	 * @param res
	 * @param id
	 * @Description：
	 */
	@RequestMapping(value = "/queryUser", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryUser(HttpServletRequest req, HttpServletResponse res, Long id){
		userService.queryUserById(id);
	}
	
	
	/**
	 * 
	 * 2018年2月12日 下午3:29:03
	 * @Author：郭
	 * @param id
	 * @param name
	 * @param obj
	 * @Description：
	 */
	@RequestMapping(value = "/argsParam", method = { RequestMethod.POST, RequestMethod.GET })
	public void argsParam(HttpServletRequest req, HttpServletResponse res, Long id, String name, int type){
		log.info("argsParam mothed！");
		userService.argsParam(name, id, type);
	}
	
	/**
	 * 新增用户接口
	 * 2018年2月28日 下午3:45:00
	 * @Author：郭
	 * @param req
	 * @param res
	 * @param user
	 * @Description：
	 */
	@RequestMapping(value = "/addUser", method = { RequestMethod.POST, RequestMethod.GET })
	public void addUser(HttpServletRequest req, HttpServletResponse res, User user){
		log.info("add user !");
		Date now = new Date();
		user.setCreateTime(now);
		user.setId(UUid.getNextLongId());
		int count = userService.addUser(user);
		try {
			if (count == 0) {
				res.getWriter().print("false");
			} else {
				res.getWriter().print("true");
			} 
		} catch (Exception e) {

		}
	}
	
}
