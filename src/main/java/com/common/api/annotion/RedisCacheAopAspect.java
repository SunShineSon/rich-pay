package com.common.api.annotion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.common.engine.FreemarkEngine;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;


public class RedisCacheAopAspect {
	
	private static Logger log = Logger.getLogger(RedisCacheAopAspect.class);
	@Resource
	private FreemarkEngine freemarkEngine;
	
	//添加FreeMarker可访问的类静态方法的字段
	static Map<String,TemplateHashModel> STATIC_CLASSES = new HashMap<String, TemplateHashModel>();
	static{ 
		try {
			BeansWrapper beansWrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModel = beansWrapper.getStaticModels();
			STATIC_CLASSES.put(Long.class.getSimpleName(), (TemplateHashModel) staticModel.get(java.lang.Long.class.getName()));
			STATIC_CLASSES.put(Integer.class.getSimpleName(), (TemplateHashModel) staticModel.get(java.lang.Integer.class.getName()));
			STATIC_CLASSES.put(java.lang.String.class.getSimpleName(), (TemplateHashModel) staticModel.get(java.lang.String.class.getName()));
			STATIC_CLASSES.put(Short.class.getSimpleName(), (TemplateHashModel) staticModel.get(java.lang.Short.class.getName()));
			STATIC_CLASSES.put(Boolean.class.getSimpleName(), (TemplateHashModel) staticModel.get(java.lang.Boolean.class.getName()));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}	
	
	public Object doCacheable(ProceedingJoinPoint point) throws Throwable{
		String methodName = point.getSignature().getName();
		//类
		Class<?> targetClass = point.getTarget().getClass();
		log.info("切面方法前缀：" + targetClass.getName());
		//方法
		Method[] methods = targetClass.getMethods();
		Method method = null;
		for (int i = 0; i < methods.length; i++){
			if (methods[i].getName() == methodName){
				method = methods[i]; 				
				break;
			}
		}
	
		//如果横切点不是方法，返回
		if (method == null){
			return point.proceed();
		}
				
		RedisCache cacheable = method.getAnnotation(RedisCache.class);
		if(cacheable != null){
			log.info("切面方法参数：" + cacheable.toString());
			String params = cacheable.params();
			String annotationKey = cacheable.key();
			
			/*
			int db = skgCacheable.db();
			int seconds = skgCacheable.seconds();
			boolean override = skgCacheable.override();
			*/
			
			//方法传递的参数值
			Object[] args = point.getArgs();
			//方法注解需求参数名
			List<String> list = toList(params, ",");
			Map<String, Object> map = new HashMap<String, Object>();
			//map.putAll(STATIC_CLASSES);	
			
			if(args.length != 0 && list.size() != 0){
				for (int i = 0; i < list.size(); i++) {
					map.put(lowerFirst(list.get(i)), args[i]);
				}
			}
			
			log.info(map.toString());
			
			try {
				String key = "";
				if(StringUtils.isNotBlank(annotationKey)){
					key = targetClass.getName() + "_" + freemarkEngine.parseByStringTemplate(annotationKey, map);		
					log.info("key = " + key);
				}else{
					
				}
			} catch (Exception e) {
				return point.proceed();
			}
			
			return null;
		}else {
			//如果方法上没有注解@Cacheable，返回
			return point.proceed();
		}
	}
	
	public static void main(String[] args) {
		String str = "id";
		String result = str.substring(0, 1).toLowerCase() + str.substring(1);
		System.out.println(result);
	}
	
	public List<String> toList(String str, String token) {
		List<String> array = new ArrayList<String>();
		if (str != null && str.length() > 0 && token != null && token.length() > 0) {
			if (str.indexOf(token) > -1) {
				StringTokenizer st = new StringTokenizer(str, token);
				while (st.hasMoreTokens()) {
					array.add(st.nextToken());
				}
			} else {
				array.add(str);
			}
		}
		return array;
	}
	
	public String lowerFirst(String str){
		String result = str.substring(0, 1).toLowerCase() + str.substring(1);
		return result;
	}
	
	
}
