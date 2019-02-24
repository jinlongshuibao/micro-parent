package com.uiotsoft.micro.user.entity;

import lombok.Data;

@Data
public class Tenant {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 租户名称
	 */
	private String name;
	/**
	 * 租户类型编码
	 */
	private String tenantTypeCode;

}
