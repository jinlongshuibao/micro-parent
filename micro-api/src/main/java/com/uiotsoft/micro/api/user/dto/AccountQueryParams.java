package com.uiotsoft.micro.api.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

@Data
@ApiModel(value = "AccountQueryParams", description = "账号查询参数")
public class AccountQueryParams {
	
	@ApiModelProperty("所属租户id")
	private Long tenantId;
	
	@ApiModelProperty("所属租户id列表")
	private List<Long> tenantIds;
	
	@ApiModelProperty("用户名(模糊匹配)")
	private String username;
	
}
