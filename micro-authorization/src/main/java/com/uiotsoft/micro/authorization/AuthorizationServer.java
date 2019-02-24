package com.uiotsoft.micro.authorization;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.uiotsoft.micro.api"})
@ComponentScan(basePackages = {"com.uiotsoft.micro"})
@MapperScan("com.uiotsoft.micro.**.dao")
@EnableHystrix
public class AuthorizationServer {
	
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServer.class, args);

	}

}
