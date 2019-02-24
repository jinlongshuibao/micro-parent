package com.uiotsoft.micro.user.entity;

import lombok.Data;


@Data
public class TenantType {
	/**
	 * 租户类型id
	 */
	private Long id;
	/**
	 * 租户类型名称
	 */
	private String name;
	/**
	 * 租户类型编码
	 */
	private String code;
}
