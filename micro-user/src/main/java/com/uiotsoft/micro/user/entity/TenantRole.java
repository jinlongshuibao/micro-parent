package com.uiotsoft.micro.user.entity;

import lombok.Data;

@Data
public class TenantRole {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 租户内角色编码
	 */
	private String roleCode;
	/**
	 * 角色名称
	 */
	private String roleName;
}
