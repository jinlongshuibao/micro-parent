package com.uiotsoft.micro.user.entity;

import lombok.Data;

@Data
public class Account {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 国际区号
	 */
	private String areaCode;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 加密盐
	 */
	private String salt;
	
	/**
	 * 账号是否删除
	 */
	private Integer isDel;

}
