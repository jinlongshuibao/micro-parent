package com.uiotsoft.micro.user.entity;

import lombok.Data;

@Data
public class AccountTenant {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 账号ID
	 */
	private String accountId;
	/**
	 * 租户ID
	 */
	private String tenantId;

}
