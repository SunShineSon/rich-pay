package com.common.api.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited
public @interface RedisCache {
	
	public int db();
	
	public String key();
	
	public String params() default "" ;
	
	public int seconds() default 60*60*24;
	
	public boolean override() default false;	//是否使用单独过期策略，默认为false（使用app-web.properties的全局策略）
	
}