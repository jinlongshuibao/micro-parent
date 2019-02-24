package com.uiotsoft.micro.resource.loader;

import java.util.List;

/**
 * 资源加载器 ，不同类型资源加载实现 ，资源统一加载为json字符串 
 */
public interface ResourceLoader {

	/**
	 * 获取当前资源加载器类型
	 * @return
	 */
	String getType();
	
	/**
	 * 加载指定应用的当前类型资源
	 * @param privileageCodes 权限编码列表
	 * @param applicationCode 应用编码
	 * @return
	 */
	String load(List<String> privileageCodes, String applicationCode);

}
