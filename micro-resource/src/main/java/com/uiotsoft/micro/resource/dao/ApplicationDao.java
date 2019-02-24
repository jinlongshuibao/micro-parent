package com.uiotsoft.micro.resource.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.entity.Application;


public interface ApplicationDao {
	
	Application selectByCode(String code);
	
	@Select("select * from resource_application")
	List<Application> findAll();
	
	/**
	 * 通过code查应用
	 * @param code
	 * @return List<Application>
	 * @author 杨小波
	 * @date 2018年08月29日 09:35:20
	 */
	List<Application> selectApplicationByCode(@Param("code")String code);
	/**
     * 通过name查应用
     * @param name
     * @return List<Application>
     * @author 杨小波
     */
    List<Application> selectApplicationByName(@Param("name")String name);
	/**
	 * 添加应用
	 * @param application
	 * @author 孔得峰
	 * @date 2018年7月30日 下午5:07:33
	 */
	void insertApplication(@Param("application")ApplicationDTO application);
	/**
	 * 修改应用
	 * @param application
	 * @author 孔得峰
	 * @date 2018年7月30日 下午5:10:15
	 */
	Integer updateApplication(@Param("application")ApplicationDTO application);
	/**
	 * 删除应用
	 * @param applicationCode
	 * @author 孔得峰
	 * @date 2018年7月30日 下午5:14:23
	 */
	Integer deleteApplication(@Param("applicationCode")String applicationCode);
	/**
	 * 根据条件获取应用分页数据
	 * @param params
	 * @param pageRequest
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午5:22:47
	 */
	List<Application> pageApplicationByConditions(@Param("params")ApplicationQueryParams params, @Param("page")PageRequestParams pageRequest);
	/**
	 * 根据条件统计应用数量
	 * @param params
	 * @param pageRequest
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月30日 下午5:25:42
	 */
	Long countApplicationByConditions(@Param("params")ApplicationQueryParams params);
}
