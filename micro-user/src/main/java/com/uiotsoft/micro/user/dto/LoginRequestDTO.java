package com.uiotsoft.micro.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LoginRequestDTO", description = "登陆租户请求信息")
public class LoginRequestDTO {
	

	@ApiModelProperty("用户名")
	private String username;
	
	@ApiModelProperty("密码")
	private String password;
	
	@ApiModelProperty("租户id")
	private String tenantId;

}
