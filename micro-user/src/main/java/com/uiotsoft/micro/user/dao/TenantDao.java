package com.uiotsoft.micro.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.user.dto.TenantDTO;
import com.uiotsoft.micro.user.dto.TenantQueryParams;
import com.uiotsoft.micro.user.dto.TenantRoleDTO;
import com.uiotsoft.micro.user.dto.TenantTypeDTO;
import com.uiotsoft.micro.user.dto.TenantTypeRoleDTO;
import com.uiotsoft.micro.user.entity.Tenant;
import com.uiotsoft.micro.user.entity.TenantRole;
import com.uiotsoft.micro.user.entity.TenantType;
import com.uiotsoft.micro.user.entity.TenantTypeRole;


public interface TenantDao {
	
	List<Tenant> pageTenantByConditions(@Param("query")TenantQueryParams queryParams, @Param("page")PageRequestParams pageRequest);
	
	Long countTenantByConditions(@Param("query")TenantQueryParams queryParams, @Param("page")PageRequestParams pageRequest);
	
	/**
	 * 添加租户类型
	 * @param tenantType
	 * @author 孔得峰
	 * @date 2018年7月25日 下午3:19:20
	 */
	void insertTenantType(TenantType tenantType);
	/**
	 * 删除租户类型
	 * @param tenantTypeCode
	 * @author 孔得峰
	 * @date 2018年7月25日 下午3:47:37
	 */
	Integer deleteTenantType(@Param("code")String tenantTypeCode);
	/**
	 * 解绑租户类型和角色类型关系
	 * @param tenantTypeCode
	 * @param roleCode void
	 * @author 杨小波
	 * @date 2018年08月16日 11:47:04
	 */
	Integer unbindRoleOfTT(@Param("tenantTypeCode") String tenantTypeCode,@Param("roleCode") String roleCode);
	/**
	 * 修改租户类型
	 * @param tenantType
	 * @author 孔得峰
	 * @date 2018年7月25日 下午4:02:32
	 */
	Integer updateTenantType(@Param("tenantType")TenantTypeDTO tenantType);
	/**
	 * 查询所有租户类型
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 下午4:12:33
	 */
	List<TenantType> selectTenantTypes();
	/**
	 * 根据租户类型编码查询租户类型
	 * @param tenantTypeCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 下午4:19:29
	 */
	TenantType selectTenantTypeByCode(@Param("code")String tenantTypeCode);
	
	/**
	 * 根据租户类型id查询租户类型
	 * @param tenantId
	 * @return TenantType
	 * @author 杨小波
	 * @date 2018年08月16日 11:16:37
	 */
	TenantType selectTenantTypeById(@Param("id")Long tenantId);
	
	/**
	 * 绑定租户类型code和角色code
	 * @param tenantTypeCode
	 * @param roleCode void
	 * @author 杨小波
	 * @date 2018年08月17日 17:10:05
	 */
	void bindRoleTypeOfTT(@Param("tenantTypeCode") String tenantTypeCode, @Param("roleCode") String roleCode);
	
	/**
	 * 添加租户类型内角色类型
	 * @param roles
	 * @author 孔得峰
	 * @date 2018年7月25日 下午4:45:20
	 */
	void insertTenantRoles(@Param("tenantType")TenantTypeDTO tenantType, @Param("roles")List<TenantTypeRoleDTO> roles);
	/**
	 * 查询租户类型角色
	 * @param tenantTypeCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月27日 上午10:09:55
	 */
	List<TenantTypeRole> selectTenantTypeRoles(@Param("code")String tenantTypeCode);
	/**
	 * 删除租户类型角色
	 * @param tenantTypeCode
	 * @author 孔得峰
	 * @date 2018年7月25日 下午5:32:19
	 */
	Integer deleteTenantTypeRoles(@Param("code")String tenantTypeCode);
	/**
	 * 添加租户
	 * @param tenant
	 * @author 孔得峰
	 * @date 2018年7月25日 下午5:40:28
	 */
	void insertTenant(Tenant tenant);
	/**
	 * 删除租户
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年7月25日 下午5:45:06
	 */
	Integer deleteTenant(@Param("id")Long tenantId);
	/**
	 * 删除租户内角色
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年7月26日 上午11:48:37
	 */
	Integer deleteTenantRoleByTenantId(@Param("id")Long tenantId);
	/**
	 * 删除该租户和账号的关联关系
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年7月26日 上午11:51:30
	 */
	Integer deleteTenantAccountByTenantId(@Param("id")Long tenantId);
	/**
	 * 修改租户
	 * @param tenant
	 * @author 孔得峰
	 * @date 2018年7月25日 下午5:50:03
	 */
	Integer updateTenant(@Param("tenant")TenantDTO tenant);
	/**
	 * 根据ID查询租户
	 * @param id
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 下午5:59:15
	 */
	Tenant selectTenantById(@Param("id")Long id);
	
	/**
	 * 根据租户名字查询租户
	 * @param name
	 * @return Tenant
	 * @author 杨小波
	 * @date 2018年09月19日 15:25:53
	 */
	List<Tenant> selectTenantByName(@Param("name") String name);
	
	/**
	 * 根据ID列表查询多租户
	 * @param ids
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 下午6:04:04
	 */
	List<Tenant> selectTenantByIds(@Param("ids")Long[] ids);
	/**
	 * 跟进用户名查询租户信息列表
	 * @param username
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月26日 上午11:15:51
	 */
	List<Tenant> selectTenantByUsername(@Param("username")String username);
	/**
	 * 跟进用户名和租户类型查询租户信息列表
	 * @param username
	 * @param tenantTypeCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月26日 上午11:20:48
	 */
	List<Tenant> selectTenantByUsernameAndTenantType(@Param("username")String username, @Param("code")String tenantTypeCode);
	/**
	 * 根据租户类型查询租户
	 * @param username
	 * @param tenantTypeCode
	 * @return List<Tenant>
	 * @author 杨小波
	 * @date 2018年08月16日 09:38:28
	 */
    List<Tenant> selectTenantByTenantType(@Param("code")String tenantTypeCode);
	/**
	 * 添加租户内角色
	 * @param tenantId
	 * @param role
	 * @author 孔得峰
	 * @date 2018年7月25日 下午6:28:43
	 */
	void insertTenantRole(@Param("tenantId")Long tenantId, @Param("role")TenantRoleDTO role);
	/**
	 * 删除租户内角色
	 * @param tenantId
	 * @param roleCode
	 * @author 孔得峰
	 * @date 2018年7月25日 下午6:35:54
	 */
	Integer deleteTenantRole(@Param("tenantId")Long tenantId, @Param("roleCode")String roleCode);
	/**
	 * 查询租户内角色列表
	 * @param tenantId
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月26日 上午10:06:31
	 */
	List<TenantRole> selectTenantRoles(@Param("tenantId")Long tenantId);
	
	
	
	
	
}
