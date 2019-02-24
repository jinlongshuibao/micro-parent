package com.uiotsoft.micro.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ModifyPasswordParams", description = "修改密码参数")
public class ModifyPasswordParams {
	
	@ApiModelProperty("用户名")
	private String username;
	
	@ApiModelProperty("新密码")
	private String  newPassword; 
	
	@ApiModelProperty("老密码")
	private String  oldPassword;

}
