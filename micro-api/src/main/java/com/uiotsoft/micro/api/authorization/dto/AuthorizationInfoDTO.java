package com.uiotsoft.micro.api.authorization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 授权信息
 */
@Data
@ApiModel(value = "PrivilegeDTO", description = "授权信息,包括角色与权限")
public class AuthorizationInfoDTO {
	
	/*private List<String>  roleCodes = new ArrayList<>();//角色编码集合
	
	private List<String> privilegeCodes = new ArrayList<>(); //权限编码集合
*/	
	@ApiModelProperty("授权信息，角色-权限关系")
	private Map<String,String[]>  rolePrivilegeMap = new HashMap<>();//角色-权限列表 映射
	
	
	


}
