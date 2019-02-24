package com.uiotsoft.micro.gateway.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(RocketmqProperties.PREFIX)
public class RocketmqProperties {
	public static final String PREFIX = "apache.rocketmq";
	
	private String namesrvAddr;
	
	public String getNamesrvAddr() {
		return namesrvAddr;
	}
	public void setNamesrvAddr(String namesrvAddr) {
		this.namesrvAddr = namesrvAddr;
	}

	
}
