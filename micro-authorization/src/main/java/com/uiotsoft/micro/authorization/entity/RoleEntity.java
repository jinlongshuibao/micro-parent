package com.uiotsoft.micro.authorization.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@ApiModel(value = "RoleEntity", description = "角色信息")
@Data
public class RoleEntity  {
	
	@ApiModelProperty("角色id")
	private Long id;

	@ApiModelProperty("角色编码")
    private String code;

	@ApiModelProperty("角色名称")
    private String name;

	@ApiModelProperty("排序")
    private Integer sort;

	@ApiModelProperty("状态")
    private Integer status;
    
	@ApiModelProperty("角色所拥有的权限列表")
    private List<PrivilegeEntity> privileges = new ArrayList<>();
	
}
