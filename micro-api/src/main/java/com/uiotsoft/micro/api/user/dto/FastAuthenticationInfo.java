package com.uiotsoft.micro.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FastAuthenticationInfo", description = "快捷认证信息")
public class FastAuthenticationInfo {
	
	@ApiModelProperty("手机号")
	private String mobile;
	
	@ApiModelProperty("快捷登录认证码")
	private String code;

}
