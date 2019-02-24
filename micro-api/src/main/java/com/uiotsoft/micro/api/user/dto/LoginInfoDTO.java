package com.uiotsoft.micro.api.user.dto;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModel;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "LoginInfoDTO", description = "登陆返回结果")
public class LoginInfoDTO {
	
	@ApiModelProperty("用户名")
	private String username;

	@ApiModelProperty("手机号")
	private String mobile;
	
	@ApiModelProperty("租户类型")
	private String tenantType;
	
	@ApiModelProperty("租户id")
	private String tenantId;
	
	@ApiModelProperty("租户名称")
	private String tenantName;
	
	@ApiModelProperty("授权信息，角色-权限关系")
	private Map<String,String[]>  rolePrivilegeMap = new HashMap<>();
	
	

}
