package com.uiotsoft.micro.resource.entity;

import lombok.Data;


@Data
public class Application {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 应用名称
	 */
    private String name;
    /**
     * 应用编码
     */
    private String code;

}
