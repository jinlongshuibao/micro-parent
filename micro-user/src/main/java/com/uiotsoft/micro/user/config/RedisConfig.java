package com.uiotsoft.micro.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.uiotsoft.micro.user.common.cache.Cache;
import com.uiotsoft.micro.user.common.cache.RedisCache;

//@Configuration
public class RedisConfig {
	
	@Bean
	public Cache cache(StringRedisTemplate redisTemplate){
		return new RedisCache(redisTemplate);
	}
	

}
