package com.uiotsoft.micro.api.authorization.dto;


import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RoleQueryParams", description = "角色查询参数")
@Data
public class RoleQueryParams {
	
	@ApiModelProperty("角色编码")
    private String code;

	@ApiModelProperty("角色名称")
    private String name;

}
