package com.uiotsoft.micro.api.authorization;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.uiotsoft.micro.api.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.api.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.api.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.api.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.api.authorization.dto.PrivilegeTreeDTO;
import com.uiotsoft.micro.api.authorization.dto.RoleDTO;
import com.uiotsoft.micro.api.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.RestResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**  
 * @author 杨小波
 * @date 2018年07月26日 09:35:16  
 */
@FeignClient(value = "micro-authorization",fallback=AuthorizationRemoteHystrix.class)
public interface AuthorizationService {
    
    /**
     * 通过权限组id获取子权限树
     * @param pgroupId
     * @return RestResponse<PrivilegeTreeDTO>
     * @author 杨小波
     * @date 2018年07月31日 11:22:40
     */
    @GetMapping(value = "/authorization/privilegeTree/privilegeGroup/{pgroupId}")
    RestResponse<PrivilegeTreeDTO> queryChildPrivilegeTree(@PathVariable("pgroupId") String pgroupId);
    
    /**
     * 获取整棵权限树
     * @return RestResponse<PrivilegeTreeDTO>
     * @author 杨小波
     * @date 2018年08月14日 10:57:07
     */
    @GetMapping(value = "/authorization/privilegeTree/roles")
    public RestResponse<PrivilegeTreeDTO> queryPrivilegeTree();
    
    /**
     * 根据角色code获取权限树
     * @param roleCodes
     * @return RestResponse<PrivilegeTreeDTO>
     * @author 杨小波
     * @date 2018年07月30日 18:57:46
     */
    @GetMapping(value = "/authorization/privilegeTree/roles/{roleCodes}")
    RestResponse<PrivilegeTreeDTO> queryPrivilegeTree(@PathVariable("roleCodes") String [] roleCodes);
    
    
    /**
     * 获取权限
     * @param owner
     * @return RestResponse<AuthorizationInfoDTO>
     * @author 杨小波
     * @date 2018年07月30日 18:56:28
     */
    @GetMapping(value = "/authorization/{owner}")
    RestResponse<AuthorizationInfoDTO> authorize(@PathVariable("owner") String owner);
    
    
    /**
     * 解绑角色至拥有者
     * @param owner
     * @param roleCodes
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月28日 17:44:33
     */
    @DeleteMapping(value = "/authorization/{owner}")
    RestResponse<Nullable> unbind(@PathVariable("owner") String owner, @RequestBody List<String> roleCodes);
    
    
    /**
     * 绑定角色至拥有者
     * @param owner
     * @param roleCodes
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月28日 17:43:58
     */
    @PostMapping(value = "/authorization/{owner}")
    RestResponse<Nullable> bind(@PathVariable("owner") String owner, @RequestBody List<String> roleCodes);
    
    
    /**
     * 分页查询角色列表(不包含权限)
     * @param queryParams
     * @param pageNo
     * @param pageSize
     * @return RestResponse<Page<RoleDTO>>
     * @author 杨小波
     * @date 2018年07月28日 17:39:01
     */
    @PostMapping(value = "/authorization/role/page")
    RestResponse<Page<RoleDTO>> pageRoleByConditions(@RequestBody RoleQueryParams queryParams
            ,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
    
    
    /**
     * 根据roleCode列表获取多个角色(不包含权限)
     * @param roleCodes
     * @return RestResponse<List<RoleDTO>>
     * @author 杨小波
     * @date 2018年07月28日 17:37:47
     */
    @GetMapping(value = "/authorization/roles/{roleCodes}")
    RestResponse<List<RoleDTO>> queryRole(@PathVariable("roleCodes") String [] roleCodes);
    
    
    /**
     * 角色绑定权限
     * @param roleCode
     * @param privilegeCodes
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月28日 17:36:37
     */
    @PutMapping(value = "/authorization/role/{roleCode}/privileges/")
    RestResponse<Nullable> roleBindPrivilege(@PathVariable("roleCode") String roleCode, @RequestBody List<String> privilegeCodes);
    
    
    /**
     * 根据roleCode获取角色(包含权限)
     * @param roleCode
     * @return RestResponse<RoleDTO>
     * @author 杨小波
     * @date 2018年07月27日 19:05:23
     */
    @GetMapping(value = "/authorization/role/{roleCode}")
    RestResponse<RoleDTO> queryRole(@PathVariable("roleCode") String roleCode);
    
    
    /**
     * 删除角色
     * @param roleCodes
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月27日 19:04:53
     */
    @DeleteMapping(value = "/authorization/role/{roleCodes}")
    RestResponse<String> removeRole(@PathVariable("roleCodes") String[] roleCodes);
    
    
    /**
     * 修改角色
     * @param role
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月27日 19:04:00
     */
    @PutMapping(value = "/authorization/role")
    RestResponse<Nullable> modifyRole(@RequestBody RoleDTO role);
    
    
    /**
     * 新创建角色并绑定权限
     * @param RoleSubDTO
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月27日 19:03:24
     */
    @PostMapping(value = "/authorization/role")
    RestResponse<RoleDTO> createRole(@RequestBody RoleDTO role);
    
    
    /**
     * 分页条件检索权限
     * @param queryParams
     * @param pageNo
     * @param pageSize
     * @return RestResponse<Page<PrivilegeDTO>>
     * @author 杨小波
     * @date 2018年07月27日 19:02:28
     */
    @PostMapping(value = "/authorization/privilege/page")
    RestResponse<Page<PrivilegeDTO>> pagePrivilegeByConditions(@RequestBody PrivilegeQueryParams queryParams
            ,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
    
    
    /**
     * 删除权限
     * @param privilegeCodes
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月27日 19:00:36
     */
    @DeleteMapping(value = "/authorization/privilege/{privilegeCodes}")
    RestResponse<Nullable> removePrivilege(@PathVariable("privilegeCodes") String[] privilegeCodes);
    
    
    /**
     * 修改权限
     * @param privilege
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月27日 18:59:53
     */
    @PutMapping(value = "/authorization/privilege")
    RestResponse<Nullable> modifyPrivilege(@RequestBody PrivilegeDTO privilege);
    
    /**
     * 根据权限组id获取权限组下权限
     * @param id
     * @return RestResponse<List<PrivilegeDTO>>
     * @author 杨小波
     * @date 2018年08月11日 16:54:38
     */
    @GetMapping(value = "/authorization/privilegeGroup/privileges/{id}")
    RestResponse<List<PrivilegeDTO>> queryPrivileges(@PathVariable("id") Long id);
    
    /**
     * 新增权限组
     * @param privilegeGroup
     */
    @PostMapping(value = "/authorization/privilegeGroup")
    RestResponse<PrivilegeGroupDTO> createPrivilegeGroup(@RequestBody PrivilegeGroupDTO privilegeGroup);
    
    
    /**
     * 修改权限组
     * @param privilegeGroup
     * @return 
     */
    @PutMapping(value = "/authorization/privilegeGroup")
    RestResponse<Nullable> modifyPrivilegeGroup(@RequestBody PrivilegeGroupDTO privilegeGroup);
    
    
    /**
     * 删除权限组
     * @param id
     * @return 
     */
    @DeleteMapping(value = "/authorization/privilegeGroup/{id}")
    RestResponse<Nullable> removePrivilegeGroup(@PathVariable("id") Long id);
    
    
    /**
     * 根据id查询权限组
     * @param id
     * @return PrivilegeGroupDTO
     */
    @GetMapping(value = "/authorization/privilegeGroup/{id}")
    RestResponse<PrivilegeGroupDTO> queryPrivilegeGroup(@PathVariable("id") Long id);
    
    
    /**
     * 获取所有权限组
     * @return RestResponse<List<PrivilegeGroupDTO>>
     * @author 杨小波
     * @date 2018年07月26日 16:13:00
     */
    @GetMapping(value = "/authorization/privilegeGroup")
    RestResponse<List<PrivilegeGroupDTO>> queryAllPrivilegeGroup();
    
    
    /**
     * 新增权限
     * @param privilege
     * @return RestResponse<Nullable>
     * @author 杨小波
     * @date 2018年07月26日 16:14:39
     */
    @PostMapping(value = "/authorization/privilege")
    RestResponse<Nullable> createPrivilege(@RequestBody PrivilegeDTO privilege);
    
    
    /**
     * 根据权限编码获取权限
     * @param privilegeCode
     * @return RestResponse<PrivilegeDTO>
     * @author 杨小波
     * @date 2018年07月26日 16:16:55
     */
    @GetMapping(value = "/authorization/privilege/{privilegeCode}")
    RestResponse<PrivilegeDTO> queryPrivilege(@PathVariable("privilegeCode") String privilegeCode);
    
}



/*
 * 熔断处理
 */
@Component
class AuthorizationRemoteHystrix implements AuthorizationService{

    @Override
    public RestResponse<PrivilegeGroupDTO> createPrivilegeGroup(PrivilegeGroupDTO privilegeGroup) {
        return new RestResponse<PrivilegeGroupDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> modifyPrivilegeGroup(PrivilegeGroupDTO privilegeGroup) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());        
    }

    @Override
    public RestResponse<Nullable> removePrivilegeGroup(Long id) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<PrivilegeGroupDTO> queryPrivilegeGroup(Long id) {
        return new RestResponse<PrivilegeGroupDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<List<PrivilegeGroupDTO>> queryAllPrivilegeGroup() {
        return new RestResponse<List<PrivilegeGroupDTO>>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> createPrivilege(PrivilegeDTO privilege) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<PrivilegeDTO> queryPrivilege(String privilegeCode) {
        return new RestResponse<PrivilegeDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<RoleDTO> queryRole(String roleCode) {
        return new RestResponse<RoleDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<String> removeRole(String[] roleCodes) {
       
        
        return new RestResponse<String>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> modifyRole(RoleDTO role) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<RoleDTO> createRole(RoleDTO role) {
        return new RestResponse<RoleDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Page<PrivilegeDTO>> pagePrivilegeByConditions(PrivilegeQueryParams queryParams, Integer pageNo,
            Integer pageSize) {
        return new RestResponse<Page<PrivilegeDTO>>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> removePrivilege(String[] privilegeCodes) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> modifyPrivilege(PrivilegeDTO privilege) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> unbind(String owner, List<String> roleCodes) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> bind(String owner, List<String> roleCodes) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Page<RoleDTO>> pageRoleByConditions(
            com.uiotsoft.micro.api.authorization.dto.RoleQueryParams queryParams, Integer pageNo, Integer pageSize) {
        return new RestResponse<Page<RoleDTO>>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<List<RoleDTO>> queryRole(String[] roleCodes) {
        return new RestResponse<List<RoleDTO>>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<Nullable> roleBindPrivilege(String roleCode, List<String> privilegeCodes) {
        return new RestResponse<Nullable>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<PrivilegeTreeDTO> queryPrivilegeTree(String[] roleCodes) {
         return new RestResponse<PrivilegeTreeDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<AuthorizationInfoDTO> authorize(String owner) {
        return new RestResponse<AuthorizationInfoDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<PrivilegeTreeDTO> queryChildPrivilegeTree(String pgroupId) {
        return new RestResponse<PrivilegeTreeDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<List<PrivilegeDTO>> queryPrivileges(Long id) {
        return new RestResponse<List<PrivilegeDTO>>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

    @Override
    public RestResponse<PrivilegeTreeDTO> queryPrivilegeTree() {
        return new RestResponse<PrivilegeTreeDTO>(ErrorCode.E_999991.getCode(), ErrorCode.E_999991.getDesc());
    }

}
