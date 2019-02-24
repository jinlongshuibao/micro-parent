package com.uiotsoft.micro.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AuthenticationInfo", description = "认证信息")
public class AuthenticationInfo {
	
	@ApiModelProperty("用户名")
	private String username;
	
	@ApiModelProperty("密码")
	private String password;

}
