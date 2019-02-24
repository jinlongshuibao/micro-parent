package com.uiotsoft.micro.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ModifyMobileParams", description = "修改手机号参数")
public class ModifyMobileParams {
	
	@ApiModelProperty("用户名")
	private String username;
	
	@ApiModelProperty("要更改的手机号")
	private String  mobile;
	
	@ApiModelProperty("老手机号收到的验证码")
	private String  code; 
	
}
