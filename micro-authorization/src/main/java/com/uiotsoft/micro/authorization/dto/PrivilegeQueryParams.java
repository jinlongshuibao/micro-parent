package com.uiotsoft.micro.authorization.dto;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PrivilegeQueryParams", description = "权限查询参数")
@Data
public class PrivilegeQueryParams {
	
	@ApiModelProperty("所属权限组id")
    private Long privilegeGroupId;

	@ApiModelProperty("权限编码")
    private String code;

	@ApiModelProperty("权限名称")
    private String name;

}
