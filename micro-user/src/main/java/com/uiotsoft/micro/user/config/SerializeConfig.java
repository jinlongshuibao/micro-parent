package com.uiotsoft.micro.user.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.uiotsoft.micro.user.common.mq.JsonSerialize;
import com.uiotsoft.micro.user.common.mq.Serialize;

/**
 *序列化配置
 * 
 */
@Configuration
public class SerializeConfig {
	
	@Bean
	public Serialize serialize(){
		return new JsonSerialize();
	}
	
}

