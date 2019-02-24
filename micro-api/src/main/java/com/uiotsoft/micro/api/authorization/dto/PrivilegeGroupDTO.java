package com.uiotsoft.micro.api.authorization.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "PrivilegeGroupDTO", description = "权限组信息")
public class PrivilegeGroupDTO {

	@ApiModelProperty("权限组id")
	private Long id;

	@ApiModelProperty("权限组父id")
	private Long parentId;

	@ApiModelProperty("权限组名称")
	private String name;

	@ApiModelProperty("排序")
	private Integer sort;
	
}
