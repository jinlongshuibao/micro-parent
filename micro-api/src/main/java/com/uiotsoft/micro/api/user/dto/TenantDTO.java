package com.uiotsoft.micro.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TenantDTO", description = "租户信息")
public class TenantDTO {
	
	@ApiModelProperty("租户id")
	private Long id;
	
	@ApiModelProperty("租户名称")
	private String name;
	
	@ApiModelProperty("租户类型编码")
	private String tenantTypeCode;
	
}
