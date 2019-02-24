package com.uiotsoft.micro.resource.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.dto.ResourceDTO;
import com.uiotsoft.micro.resource.loader.ResourceLoader;

/**
 * 资源服务
 */
public interface ResourceService {
	
	/**
	 * 创建应用
	 * @param application
	 */
	void createApplication(ApplicationDTO application);
	
	/**
	 * 修改应用
	 * @param application
	 */
	void modifyApplication(ApplicationDTO application);
	
	/**
	 * 删除应用
	 * @param applicationCode
	 */
	void removeApplication(String applicationCode);
	
	/**
	 * 根据应用编码查找应用
	 * @param applicationCode
	 * @return
	 */
	ApplicationDTO queryApplication(String applicationCode);
	
	/**
	 * 条件分页查找应用
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<ApplicationDTO> pageApplicationByConditions(ApplicationQueryParams query,
			Integer pageNo, Integer pageSize);
	
	/**
	 * 加载指定应用的资源
	 * @param privileageCodes
	 * @param applicationCode
	 * @return
	 */
	ResourceDTO loadResources(List<String> privileageCodes, String applicationCode);
	
	/**
	 * 获取资源加载器列表，可以有menu、app资源、数据URL、设备权限
	 * @return
	 */
	List<ResourceLoader> getResourceLoaders();
	

}
