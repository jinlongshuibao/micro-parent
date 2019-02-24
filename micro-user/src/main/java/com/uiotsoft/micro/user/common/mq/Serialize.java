package com.uiotsoft.micro.user.common.mq;

/**
 * 序列化接口
 */
public interface Serialize {

	String serializationObject(Object obj);
	
	Object deserializationObject(String strs, Class<?> Clazz);
}
