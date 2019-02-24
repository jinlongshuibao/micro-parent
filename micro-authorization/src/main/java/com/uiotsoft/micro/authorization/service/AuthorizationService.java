package com.uiotsoft.micro.authorization.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.uiotsoft.micro.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.authorization.dto.PrivilegeTreeDTO;
import com.uiotsoft.micro.authorization.dto.RoleDTO;
import com.uiotsoft.micro.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.authorization.dto.RoleSubDTO;

/**
 * 授权服务
 */
public interface AuthorizationService {
	
    /**
     * 通过权限组id获取子权限树
     * @param pgroupId 权限组ID
     * @return
     */
    PrivilegeTreeDTO queryChildPrivilegeTree(String pgroupId);
    
	/**
	 * 获取权限
	 * @param owner 权限拥有者
	 * @return
	 */
	AuthorizationInfoDTO authorize(String owner);
	
	/**
	 * 查找并组装权限树
	 * @param roleCodes 角色编码列表，为null时代表所有权限
	 * @return
	 */
	PrivilegeTreeDTO queryPrivilegeTree(String[] roleCodes);
	/**
	 * 查找权限树原始信息
	 * @param roleCodes
	 * @return List<PrivilegeTreeDTO>
	 * @author 杨小波
	 * @date 2018年07月28日 17:17:45
	 */
	List<PrivilegeTreeDTO> queryPTree(String... roleCodes);
	
	/**
	 * 绑定角色
	 * @param owner 权限拥有者
	 * @param roleCodes 角色列表
	 * @return
	 */
	void bind(String owner, String [] roleCodes);
	
	/**
	 * 解绑角色
	 * @param owner 权限拥有者
     * @param roleCodes 角色列表
     * @return
     */
    void unbind(String owner, String [] roleCodes);
	
	// ////////////////////////////////////////////////权限组///////////////////////////////////////////////////
	
	/**
	 * 新增权限组
	 * @param privilegeGroup
	 */
    PrivilegeGroupDTO createPrivilegeGroup(PrivilegeGroupDTO privilegeGroup);
	
	/**
	 * 修改权限组
	 * @param privilegeGroup
	 */
	void modifyPrivilegeGroup(PrivilegeGroupDTO privilegeGroup);
	
	/**
	 * 删除权限组
	 * @param id
	 */
	void removePrivilegeGroup(Long id);
	
	/**
	 * 根据id获取权限组
	 * @param id
	 * @return
	 */
	PrivilegeGroupDTO queryPrivilegeGroup(Long id);
	
	/**
	 * 获取所有权限组
	 * @return
	 */
	List<PrivilegeGroupDTO> queryAllPrivilegeGroup();
	
	
	// ////////////////////////////////////////////////权限///////////////////////////////////////////////////
	
	/**
	 * 根据权限组id获取所有权限
	 * @param id
	 * @return List<PrivilegeDTO>
	 * @author 杨小波
	 * @date 2018年08月11日 16:39:49
	 */
	List<PrivilegeDTO> queryPrivileges(Long id);
	
	/**
	 * 新增权限
	 * @param privilege
	 * @return PrivilegeDTO
	 * @author 杨小波
	 * @date 2018年08月11日 16:39:49
	 */
	PrivilegeDTO createPrivilege(PrivilegeDTO privilege);
	
	/**
     * 修改权限
     * @param privilege
     * @return PrivilegeDTO
     * @author 杨小波
     * @date 2018年08月11日 16:39:49
     */
	void modifyPrivilege(PrivilegeDTO privilege);
	
	/**
     * 一组权限code删除权限
     * @param privilege
     * @return PrivilegeDTO
     * @author 杨小波
     * @date 2018年08月11日 16:39:49
     */
	void removePrivilege(String[] privilegeCodes);
	
	/**
     * 权限code新增权限
     * @param privilege
     * @return PrivilegeDTO
     * @author 杨小波
     * @date 2018年08月11日 16:39:49
     */
	PrivilegeDTO queryPrivilege(String privilegeCode);
	
	/**
     * 分页查询权限
     * @param privilege
     * @return PrivilegeDTO
     * @author 杨小波
     * @date 2018年08月11日 16:39:49
     */
	Page<PrivilegeDTO> pagePrivilegeByConditions(PrivilegeQueryParams query,
			Integer pageNo, Integer pageSize);
	
	
	// ////////////////////////////////////////////////角色、权限///////////////////////////////////////////////////
	
	/**
	 * 创建角色并绑定权限
	 * @param dto
	 */
	RoleDTO createRole(RoleDTO dto);
	
	/**
	 * 角色绑定权限
	 * @param roleCode
	 * @param privilegeCodes
	 */
	void roleBindPrivilege(String roleCode, String [] privilegeCodes);
	
	/**
	 * 修改角色并绑定权限
	 * @param role
	 */
	void modifyRole(RoleDTO role);
	
	/**
	 * 删除角色
	 * @param roleCodes
	 */
	void removeRole(String [] roleCodes);
	
	/**
	 * 根据roleCode获取角色(包含权限)
	 * @param roleCode
	 * @return
	 */
	RoleDTO queryRole(String roleCode);
	
	/**
	 * 根据roleCode列表获取多个角色(不包含权限)
	 * @param roleCodes
	 * @return
	 */
	List<RoleDTO> queryRole(String ...roleCodes);
	
	/**
	 * 分页查询角色列表(不包含权限)
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<RoleDTO> pageRoleByConditions(RoleQueryParams query,
			Integer pageNo, Integer pageSize);
	
	
	
	
}
