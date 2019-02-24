package com.uiotsoft.micro.api.user.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "TenantTypeDTO", description = "租户类型信息")
public class TenantTypeDTO {
	
	@ApiModelProperty("租户类型id")
	private Long id;
	
	@ApiModelProperty("租户类型编码")
	private String code;
	
	@ApiModelProperty("租户类型名称")
	private String name;
	
	@ApiModelProperty("租户类型权限范围")
	private List<TenantTypeRoleDTO> roles = new ArrayList<>(); 
	
}
