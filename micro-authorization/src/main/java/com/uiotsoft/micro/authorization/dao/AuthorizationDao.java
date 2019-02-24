package com.uiotsoft.micro.authorization.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uiotsoft.micro.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.authorization.entity.PrivilegeEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeGroupEntity;
import com.uiotsoft.micro.authorization.entity.PrivilegeTreeEntity;
import com.uiotsoft.micro.authorization.entity.RoleEntity;
import com.uiotsoft.micro.common.domain.PageRequestParams;


public interface AuthorizationDao {
    
    
    /**
     * 查询所有权限组（以List<PrivilegeTreeDTO>返回）
     * @return List<PrivilegeTreeEntity>
     * @author 杨小波
     * @date 2018年07月30日 15:51:42
     */
    List<PrivilegeTreeEntity> queryPG();
    
    /**
     * 通过角色code查询权限
     * @return List<PrivilegeTreeEntity>
     * @author 杨小波
     * @date 2018年07月28日 18:56:41
     */
    List<PrivilegeTreeEntity> queryPTByRC(@Param("roleCodes") String[] roleCodes);
    
    /**
     * 查询该owner对应的role的id的list
     * @return List<String>
     * @author 杨小波
     * @date 2018年07月30日 10:40:15
     */
    List<RoleEntity> queryRoleOfOwner(@Param("owner") String owner);
    
    /**
     * 通过roleid查找每个role对应的权限List
     * @param long1
     * @return List<String>
     * @author 杨小波
     * @date 2018年07月30日 10:49:17
     */
    List<String> queryPrivilegesOfRole(@Param("id")Long long1);
    
    /**
     * 通过owner删除owner_role关系
     * @param owner Integer
     * @author 杨小波
     * @date 2018年07月28日 15:04:25
     */
    Integer removeOwner_role(@Param("owner") String owner);
    
    /**
     * 通过owner查询对应的role
     * @param owner
     * @return String[]
     * @author 杨小波
     * @date 2018年07月28日 14:52:28
     */
    List<String> queryOwner_role(@Param("owner") String owner);
    
    /**
     * 解绑角色
     * @param owner
     * @param roleCodes Integer
     * @author 杨小波
     * @date 2018年07月28日 14:18:07
     */
    Integer unbind(@Param("owner")String owner, @Param("roleCodes") String[] roleCodes);
    
    /**
     * 绑定角色至拥有者
     * @param owner
     * @param roleCodes Integer
     * @author 杨小波
     * @date 2018年07月28日 11:17:13
     */
    Integer bind(@Param("owner")String owner, @Param("roleCodes") List<String> roleCodes);
    
    /**
     * 条件查询角色总数
     * @param query
     * @return Long
     * @author 杨小波
     * @date 2018年07月28日 10:57:27
     */
    Long countRoleByConditions(@Param("query") RoleQueryParams query);
    
    /**
     * 分页查询角色列表(不包含权限)
     * @param query
     * @param pageRequest
     * @return List<RoleEntity>
     * @author 杨小波
     * @date 2018年07月28日 10:53:12
     */
    List<RoleEntity> pageRoleByConditions(
            @Param("query") RoleQueryParams query,
            @Param("page")PageRequestParams pageRequest);
    
    
    /**
     * 查询角色和owner的绑定关系
     * @param roleCodes
     * @return List<String>
     * @author 杨小波
     * @date 2018年08月10日 11:00:32
     */
    List<String> queryO_Role(String[] roleCodes);
    
    /**
     * 根据角色id批量删除角色-权限关系
     * @param ids Integer
     * @author 杨小波
     * @date 2018年07月27日 18:45:40
     */
    Integer removeRole_pri(@Param("roles") List<RoleEntity> ids);
    
    /**
     * 根据角色id批量删除角色
     * @param ids Integer
     * @author 杨小波
     * @date 2018年07月27日 18:44:42
     */
    Integer removeRolesByIds(@Param("roles") List<RoleEntity> ids);
    
    /**
     * 根据code批量查询角色id
     * @param roleCodes
     * @return List<Long>
     * @author 杨小波
     * @date 2018年07月27日 18:44:46
     */
    List<RoleEntity> queryRolesByCodes(String[] roleCodes);
    
    /**
     * 修改角色信息
     * @param role Integer
     * @author 杨小波
     * @date 2018年07月27日 17:09:13
     */
    Integer modifyRole(@Param("role")RoleEntity role);
    
    /**
     * 根据roleid删除role-privilege关系
     * @param id Integer
     * @author 杨小波
     * @date 2018年07月27日 16:59:09
     */
    Integer removePrivilegesByRoleId(Long id);
    
    /**
     * 查询角色（添加修改判断重复）
     * @param code
     * @return List<RoleEntity>
     * @author 杨小波
     * @date 2018年08月27日 11:14:07
     */
    List<RoleEntity> selectRoleByCode(String code);
    
    /**
     * 新创建角色
     * @param role 
     * @author 杨小波
     * @date 2018年07月27日 14:27:06
     */
    void createRole(RoleEntity role);
    
    /**
     * 通过id查询角色
     * @param id
     * @return RoleEntity
     * @author 杨小波
     * @date 2018年08月27日 16:36:36
     */
    RoleEntity selectRoleById(@Param("id")Long id);
    
    /**
     * 角色绑定权限
     * @param Long roleId,List<Long> privilegeId
     * @author 杨小波
     * @date 2018年07月27日 14:41:28
     */
    Integer roleBindPrivilege(@Param("roleId") Long roleId, @Param("privileges")List<PrivilegeEntity> privileges);
    
    /**
     * 权限编码数组查询一组权限
     * @param privilegeCodes
     * @return List<RoleEntity>
     * @author 杨小波
     * @date 2018年07月27日 14:50:12
     */
    List<PrivilegeEntity> queryPrivilegesByCodearray(@Param("pCodes") String[] pCodes);
    
    /**
     * 一组权限对象（使用编码）查询一组权限
     * @param privilegeCodes
     * @return List<RoleEntity>
     * @author 杨小波
     * @date 2018年07月27日 14:50:12
     */
    List<PrivilegeEntity> queryPrivileges(@Param("list") List<PrivilegeEntity> privileges);
    
    /**
     * 根据roleCode获取角色
     * @param roleCode
     * @return RoleDTO
     * @author 杨小波
     * @date 2018年07月27日 09:57:07
     */
    RoleEntity queryRole(String roleCode);
    /**
     * 根据角色id查询角色权限
     * @param id
     * @return List<PrivilegeEntity>
     * @author 杨小波
     * @date 2018年07月27日 10:05:15
     */
    List<PrivilegeEntity> queryRolePrivilege(Long roleId);
    
    
    /**
     * 分页条件查询权限
     * @param query
     * @param pageNo
     * @param pageSize
     * @return List<PrivilegeEntity>
     * @author 杨小波
     * @date 2018年07月26日 18:33:43
     */
    List<PrivilegeEntity> pagePrivilegeByConditions(
            @Param("query") PrivilegeQueryParams query,
            @Param("page")PageRequestParams pageRequest);
    
    /**
     * 条件查询权限总数
     * @param query
     * @param pageRequest
     * @return Long
     * @author 杨小波
     * @date 2018年07月26日 19:14:52
     */
    Long countePrivilegeByConditions(
            @Param("query") PrivilegeQueryParams query);
    
    /**
     * 通过一组权限编码删除权限 
     * @param privilegeCodes Integer
     * @author 杨小波
     * @date 2018年07月26日 17:41:30
     */
    Integer removePrivilege(String[] privilegeCodes);
    
    /**
     * 查询角色和权限是否有绑定关系
     * @return List<Integer>
     * @author 杨小波
     * @date 2018年08月10日 10:36:36
     */
    List<Integer> queryR_Pri(String[] privilegeCodes); 
    
    
    /**
     * 
     * @param name
     * @return List<PrivilegeGroupEntity>
     * @author 杨小波
     * @date 2018年08月27日 10:11:20
     */
    List<PrivilegeGroupEntity> selectPrivilegeGroupByName(@Param("name") String name);
    
    /**
     * 新增权限组并返回新增数据的id
     * @param privilegeGroup
     * @author 杨小波
     * @date 2018年07月25日 10:31:59
     */
    void createPrivilegeGroup(PrivilegeGroupEntity privilegeGroup);
    
    /**
     * 修改权限组
     * @param privilegeGroup Integer
     * @author 杨小波
     * @date 2018年07月25日 17:51:12
     */
    Integer modifyPrivilegeGroup(@Param("pg")PrivilegeGroupEntity privilegeGroup);
    
    /**
     * 根据id删除权限组
     * @param id 
     * @return Integer
     * @author 杨小波
     * @date 2018年07月25日 18:55:03
     */
    Integer removePrivilegeGroup(@Param("id") Long id);
    
    
    /**
     * 根据权限组id查询子权限
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年08月01日 10:50:45
     */
    List<PrivilegeEntity> queryChildrenPrivilege(@Param("id") Long id);
    
    /**
     * 根据传入id查询子权限组
     * @param id
     * @return PrivilegeGroupEntity
     * @author 杨小波
     * @date 2018年08月01日 10:45:29
     */
    List<PrivilegeGroupEntity> queryChildPG(@Param("id") Long id);
    
    /**
     * 根据id获取权限组
     * @param id
     * @return PrivilegeGroupEntity
     * @author 杨小波
     * @date 2018年07月26日 10:21:18
     */
    PrivilegeGroupEntity queryPrivilegeGroup(Long id);
    
    /**
     * 查询所有权限组
     * @return List<PrivilegeGroupEntity>
     * @author 杨小波
     * @date 2018年07月26日 14:09:41
     */
    List<PrivilegeGroupEntity> queryAllPrivilegeGroup();
    
    /**
     * 根据name查询权限（添加修改时判断重复）
     * @param String
     * @return List<PrivilegeEntity>
     * @author 杨小波
     * @date 2018年08月27日 10:41:07
     */
    List<PrivilegeEntity> selectPrivilegeEntityByName(@Param("name")String name);
    
    /**
     * 新增权限
     * @param privilege
     * @author 杨小波
     * @date 2018年07月26日 14:38:28
     */
    void createPrivilege(PrivilegeEntity privilege);
    
    /**
     * 通过id查询权限
     * @param id
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年08月27日 16:20:37
     */
    PrivilegeEntity selectPrivilegeById(Long id);
    
    /**
     * 修改权限
     * @param privilege Integer
     * @author 杨小波
     * @date 2018年07月26日 16:37:34
     */
    Integer modifyPrivilege(@Param("pe")PrivilegeEntity privilege);
    
    /**
     * 根据权限编码获取权限
     * @param privilegeCode
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年07月26日 15:25:35
     */
    PrivilegeEntity queryPrivilege(String privilegeCode);
}
