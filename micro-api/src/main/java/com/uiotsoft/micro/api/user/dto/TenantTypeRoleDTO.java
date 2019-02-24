package com.uiotsoft.micro.api.user.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "TenantTypeRoleDTO", description = "租户类型角色信息")
@Data
public class TenantTypeRoleDTO {
	
	@ApiModelProperty("角色编码")
	private String roleCode;
	
	@ApiModelProperty("角色名称")
	private String roleName;
	
	@ApiModelProperty("角色所拥有的权限列表")
	private List<String> privilegeCodes = new ArrayList<>();
	
}
