package com.uiotsoft.micro.user.common.mq;

import com.alibaba.fastjson.JSON;

public class JsonSerialize implements Serialize {

	@Override
	public String serializationObject(Object obj) {
		return JSON.toJSONString(obj);
	}

	@Override
	public Object deserializationObject(String Strs, Class<?> clazz) {
		return JSON.parseObject(Strs, clazz);
	}

}
