package com.uiotsoft.micro.resource.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.dto.MenuQueryParams;
import com.uiotsoft.micro.resource.entity.Menu;


/**
 * @author 孔得峰
 * @date 2018年7月30日 下午4:07:10
 * 
 */
public interface MenuDao {
    /**
     * 判断是否有子节点
     * @param id
     * @return Menu
     * @author 杨小波
     * @date 2018年08月15日 15:47:08
     */
    List<Menu> selectChildMenu(@Param("id")Long id);
    
    /**
     * 通过code查询菜单
     * @param code
     * @return Menu
     * @author 杨小波
     * @date 2018年08月29日 09:23:57
     */
    List<Menu> selectMenuByCode(@Param("code") String code);
    
	/**
	 * 添加菜单
	 * @param menuDTO
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:21:02
	 */
	void insertMenu(@Param("menu")MenuDTO menuDTO);
	/**
	 * 修改菜单
	 * @param menuDTO
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:28:38
	 */
	Integer updateMenu(@Param("menu")MenuDTO menuDTO);
	/**
	 * 删除菜单
	 * @param id
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:45:00
	 */
	Integer deleteMenu(@Param("id")Long id);
	/**
	 * 根据ID查询菜单
	 * @param id
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:47:24
	 */
	Menu selectMenuById(@Param("id")Long id);
	/**
	 * 根据应用编码获取菜单列表
	 * @param applicationCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午4:51:15
	 */
	List<Menu> selectMenuByApplicationCode(@Param("applicationCode")String applicationCode);
	/**
	 * 根据条件获取菜单列表（分页）
	 * @param params
	 * @param pageRequest
	 * @return
	 * @author 孔得峰
	 * @date 2018年8月13日 下午3:32:34
	 */
	List<Menu> pageMenuByConditions(@Param("params")MenuQueryParams params, @Param("page")PageRequestParams pageRequest);
	/**
	 * 获取菜单总数
	 * @param params
	 * @return
	 * @author 孔得峰
	 * @date 2018年8月13日 下午3:33:01
	 */
	Long countAccountByConditions(@Param("params")MenuQueryParams params);

}
