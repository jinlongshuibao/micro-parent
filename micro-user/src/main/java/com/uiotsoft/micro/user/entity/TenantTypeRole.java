package com.uiotsoft.micro.user.entity;

import lombok.Data;

@Data
public class TenantTypeRole {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 租户类型编码
	 */
	private String tenantTypeCode;
	/**
	 * 租户类型角色编码
	 */
	private String roleCode;
}
