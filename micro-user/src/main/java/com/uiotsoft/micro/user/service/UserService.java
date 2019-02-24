package com.uiotsoft.micro.user.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.uiotsoft.micro.user.dto.AuthenticationInfo;
import com.uiotsoft.micro.user.dto.FastAuthenticationInfo;
import com.uiotsoft.micro.user.dto.LoginInfoDTO;
import com.uiotsoft.micro.user.dto.LoginRequestDTO;
import com.uiotsoft.micro.user.dto.ModifyMobileParams;
import com.uiotsoft.micro.user.dto.ModifyPasswordParams;
import com.uiotsoft.micro.user.dto.TenantCreationParam;
import com.uiotsoft.micro.user.dto.TenantRoleDTO;
import com.uiotsoft.micro.user.dto.TenantDTO;
import com.uiotsoft.micro.user.dto.TenantQueryParams;
import com.uiotsoft.micro.user.dto.TenantTypeDTO;
import com.uiotsoft.micro.user.dto.AccountDTO;
import com.uiotsoft.micro.user.dto.AccountQueryParams;

/**
 * 用户服务 
 * 注意：租户id要冗余至业务信息
 */
public interface UserService {
	
    /**
     * 创建租户并创建账号(以及绑定一系列关系)
     * @param creationParam
     * @return TenantCreationParam
     * @author 杨小波
     * @date 2018年09月14日 16:19:26
     */
    TenantCreationParam createTenantAndAccount(TenantCreationParam creationParam);
    
	/**
	 * 用户认证
	 * @param authenticationInfo
	 */
	AccountDTO authentication(AuthenticationInfo authenticationInfo);
	
	/**
	 * 用户快捷认证
	 * @param authenticationInfo
	 */
	AccountDTO fastAuthentication(FastAuthenticationInfo authenticationInfo);

	/**
	 * 用户登录租户
	 * 
	 * @return
	 */
	LoginInfoDTO login(LoginRequestDTO loginRequest);

	/**
	 * 查询用户所属租户信息列表
	 * 
	 * @param username
	 * @return
	 */
	List<TenantDTO> queryTenantByUserName(String username);

	/**
	 * 查询用户所属的某类型租户信息列表
	 * @param username
	 * @param tenantTypeCode
	 * @return
	 */
	List<TenantDTO> queryTenant(String username, String tenantTypeCode);

	
	//////////////////////////////////////////////////租户类型///////////////////////////////////////////////////
	/**
	 * 创建租户类型 (包括创建角色)
	 * 
	 * @param tenantType
	 */
	TenantTypeDTO createTenantType(TenantTypeDTO tenantType);

	/**
	 * 删除租户类型 (包括删除角色)
	 * 
	 * @param tenantTypeCode
	 */
	void removeTenantType(String tenantTypeCode);

	/**
	 * 修改租户类型 (不  包括修改角色)
	 * 
	 * @param tenantType
	 */
	void modifyTenantType(TenantTypeDTO tenantType);

	/**
	 * 查询所有租户类型(不包含权限)
	 */
	List<TenantTypeDTO> queryAllTenantType();

	/**
	 * 查询某租户类型
	 */
	TenantTypeDTO queryTenantType(String tenantTypeCode);
	
	/**
	 * 绑定租户类型和角色关系
	 * @param tenantTypeCode
	 * @param roleCode void
	 * @author 杨小波
	 * @date 2018年08月16日 14:56:11
	 */
	void bindRoleTypeOfTT(String tenantTypeCode, String roleCode);
	
	/**
	 * 解绑租户类型和角色关系
	 * @param tenantTypeCode
	 * @param roleCode void
	 * @author 杨小波
	 * @date 2018年08月16日 14:56:00
	 */
	void unbindRoleTypeOfTT(String tenantTypeCode, String roleCode);

	//////////////////////////////////////////////////租户///////////////////////////////////////////////////

	/**
	 * 创建租户
	 * 
	 * @param tenant
	 */
	TenantDTO createTenant(TenantDTO tenant);

	/**
	 * 删除租户(清除与账号关联关系,清除角色，清除角色与账号关联关系)
	 * 
	 * @param tenantId
	 */
	void removeTenant(Long tenantId);

	/**
	 * 修改租户(租户类型禁止修改)
	 * 
	 * @param tenant
	 */
	void modifyTenant(TenantDTO tenant);

	/**
	 * 查询某租户
	 * 
	 * @param id
	 * @return
	 */
	TenantDTO queryTenant(Long id);

	/**
	 * 查询多个租户
	 * 
	 * @param ids
	 *            id列表
	 * @return
	 */
	List<TenantDTO> queryTenant(Long[] ids);

	/**
	 * 类型查找租户
	 * 
	 * @param id
	 * @return
	 */
	Page<TenantDTO> pageTenantByConditions(TenantQueryParams query, Integer pageNo, Integer pageSize);

	/**
	 * 创建租户内角色
	 * 
	 * @param tenantId
	 * @param role
	 */
	void createTenantRole(Long tenantId, TenantRoleDTO role);

	/**
	 * 删除租户内角色
	 * 
	 * @param tenantId
	 * @param role
	 */
	void removeTenantRole(Long tenantId, String roleCode);
	
	/**
	 * 修改租户内角色
	 * 
	 * @param tenantId
	 * @param role
	 */
	void modifyTenantRole(Long tenantId, TenantRoleDTO role);

	/**
	 * 查询某租户下角色
	 * 
	 * @param tenantId
	 * @return
	 */
	List<TenantRoleDTO> queryTenantRole(Long tenantId);
	
	/**
	 * 查询租户内角色信息
	 * @param tenantId
	 * @param roleCode
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月27日 下午6:44:17
	 */
	TenantRoleDTO queryTenantRole(Long tenantId, String roleCode);
	//////////////////////////////////////////////////用户///////////////////////////////////////////////////

	/**
	 * 创建账号
	 * @param account
	 */
	void createAccount(AccountDTO account);
	/**
	 * 账号绑定租户
	 * @param username
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年8月16日 上午11:41:45
	 */
	 void bind(String username, String tenantId);
	/**
	 * 修改密码
	 * @param username
	 */
	void modifyAccountPassword(ModifyPasswordParams modifyPasswordParams);
	/**
	 * 修改密码无需旧密码
	 * @param username
	 */
	void modifyAccountPasswordNoOld(ModifyPasswordParams modifyPasswordParams);
	/**
	 * 找回密码
	 * @param username
	 */
	void findAccountPassword(ModifyPasswordParams modifyPasswordParams);
	
	 /** 修改手机号
	 * @param username
	 */
	void modifyAccountMobile(ModifyMobileParams modifyMobileParams);

	/**
	 * 删除账号
	 * @param username
	 */
	void removeAccount(String username);
	
	/**
	 * 账号解绑租户
	 * @param username
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年8月16日 下午2:07:40
	 */
	void unbind(String username, String tenantId);
	
	/**
	 * 查询账号
	 * @param username
	 * @return
	 */
	AccountDTO queryAccount(String username);
	
	/**
	 * 根据手机号查询账号
	 * @param mobile
	 * @return
	 */
	List<AccountDTO> queryAccountByMobile(String mobile);
	
	/**
	 * 查询账号
	 * @param usernames
	 * @return
	 */
	List<AccountDTO> queryAccount(String[] usernames);
	
	/**
	 * 分页条件查询账号列表
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<AccountDTO> pageAccountByConditions(AccountQueryParams query, Integer pageNo, Integer pageSize);
	
	/**
	 * 用户名是否存在
	 * @param username
	 * @return
	 */
	boolean isExistedUsername(String username);

	
	

}
