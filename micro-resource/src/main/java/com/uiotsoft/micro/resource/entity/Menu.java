package com.uiotsoft.micro.resource.entity;

import lombok.Data;

/**
 * 菜单信息
 * @author 孔得峰
 * @date 2018年7月30日 下午4:09:33
 * 
 */
@Data
public class Menu {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 父级ID
	 */
	private Long parentId;
	/**
	 * 菜单标题
	 */
	private String title;
	/**
	 * 菜单URL地址
	 */
	private String url;
	
	/**
	 * 页面标识
	 */
    private String code;
	/**
	 * 菜单图标
	 */
	private String icon;
	/**
	 * 菜单排序
	 */
	private Integer sort;
	/**
	 * 菜单说明
	 */
	private String comment;
	/**
	 * 菜单状态
	 */
	private Integer status;
	/**
	 * 所属应用编码
	 */
	private String applicationCode;
	/**
	 * 所属应用
	 */
	private String application;
	/**
	 * 绑定权限编码
	 */
	private String privilegeCode;
	
}
