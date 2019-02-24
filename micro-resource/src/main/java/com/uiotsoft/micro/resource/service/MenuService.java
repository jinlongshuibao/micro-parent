package com.uiotsoft.micro.resource.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.dto.MenuQueryParams;


/**
 * 菜单服务(资源服务内部使用)
 */
public interface MenuService {
	/**
	 * 创建菜单
	 * @param menuDTO
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:03:44
	 */
	void createMenu(MenuDTO menuDTO);
	/**
	 * 修改菜单
	 * @param menuDTO
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:03:52
	 */
	void modifyMenu(MenuDTO menuDTO);
	/**
	 * 删除菜单
	 * @param id
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:04:00
	 */
	void removeMenu(Long id);
	/**
	 * 根据ID查询菜单
	 * @param id
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:04:06
	 */
	MenuDTO queryMenu(Long id);
	/**
	 * 根据应用编码查询菜单列表
	 * @param applicationCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:04:18
	 */
	List<MenuDTO> queryMenuByApplicationCode(String applicationCode);
	/**
	 * 根据条件查询菜单列表
	 * @param query
	 * @return
	 * @author 孔得峰
	 * @param pageSize 
	 * @param pageNo 
	 * @date 2018年7月30日 下午4:04:31
	 */
	Page<MenuDTO> queryMenu(MenuQueryParams params, Integer pageNo, Integer pageSize);
	
}
