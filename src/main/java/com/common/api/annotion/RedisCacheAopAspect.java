package com.common.api.annotion;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;


public class RedisCacheAopAspect {
	
	private static Logger log = Logger.getLogger(RedisCacheAopAspect.class);
	
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
				
		RedisCache skgCacheable = method.getAnnotation(RedisCache.class);
		if(skgCacheable != null){
			int db = skgCacheable.db();
			int seconds = skgCacheable.seconds();
			boolean override = skgCacheable.override();
			String key = skgCacheable.key();
			String params = skgCacheable.params();
			
			log.info("数据库：" + db);
			log.info("时间：" + seconds);
			log.info("是否覆盖：" + override);
			log.info("key：" + key);
			log.info("params：" + params);
			return null;
		}else {
			//如果方法上没有注解@Cacheable，返回
			return point.proceed();
		}
	}
	
}
