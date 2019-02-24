package com.uiotsoft.micro.user.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel(value = "TenantQueryParams", description = "租户查询参数")
public class TenantQueryParams{

	@ApiModelProperty("租户名称")
	private String name;
	
	@ApiModelProperty("租户类型编码")
	private String tenantTypeCode;
	
	@ApiModelProperty("租户类型编码列表")
	private String [] tenantTypeCodes;
	
}
