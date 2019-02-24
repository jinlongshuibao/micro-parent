package com.uiotsoft.micro.authorization.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.uiotsoft.micro.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeGroupDTO;
import com.uiotsoft.micro.authorization.dto.PrivilegeQueryParams;
import com.uiotsoft.micro.authorization.dto.PrivilegeTreeDTO;
import com.uiotsoft.micro.authorization.dto.RoleDTO;
import com.uiotsoft.micro.authorization.dto.RoleQueryParams;
import com.uiotsoft.micro.authorization.service.AuthorizationService;
import com.uiotsoft.micro.common.domain.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/authorization")
@Api(value = "授权服务", tags = "AuthorizationApi", description="授权服务操作API")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    
     
    @ApiOperation("通过权限组id获取子权限树")
    @ApiImplicitParam(name = "pgroupId", value = "权限组id", required = true, dataType = "String", paramType="path")
    @GetMapping(value = "/privilegeTree/privilegeGroup/{pgroupId}")
    public RestResponse<PrivilegeTreeDTO> queryChildPrivilegeTree(@PathVariable("pgroupId") String pgroupId){
        PrivilegeTreeDTO privilegeTree = authorizationService.queryChildPrivilegeTree(pgroupId);
        return RestResponse.success(privilegeTree);
    }
    
    
    @ApiOperation("根据权限组id获取权限组下权限")
    @ApiImplicitParam(name = "id", value = "权限组id", required = true, dataType = "Long", paramType="path")
    @GetMapping(value = "/privilegeGroup/privileges/{id}")
    public RestResponse<List<PrivilegeDTO>> queryPrivileges(@PathVariable("id") Long id) {
        List<PrivilegeDTO> list = authorizationService.queryPrivileges(id);
        return RestResponse.success(list);
    }
     
	@ApiOperation("获取账号下权限")
	@ApiImplicitParam(name = "owner", value = "权限拥有者", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/{owner}")
	public RestResponse<AuthorizationInfoDTO> authorize(@PathVariable("owner") String owner) {
		AuthorizationInfoDTO authorizationInfo = authorizationService.authorize(owner);
		return RestResponse.success(authorizationInfo);
	}
	
	 
	@ApiOperation("绑定角色至拥有者")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "owner", value = "权限拥有者", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "roleCodes", value = "角色编码列表", required = true, dataTypeClass = List.class, paramType="body")
	})
	@PostMapping(value = "/{owner}")
	public RestResponse<Nullable> bind(@PathVariable("owner") String owner, @RequestBody List<String> roleCodes){
	    String[] strings = new String[roleCodes.size()];
	    roleCodes.toArray(strings);
	    authorizationService.bind(owner, strings);
	    return RestResponse.success();
	}
	
	 
	@ApiOperation("解绑角色至拥有者")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "owner", value = "权限拥有者", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "roleCodes", value = "角色编码列表", required = true, dataTypeClass = List.class, paramType="body")
	})
	@DeleteMapping(value = "/{owner}")
	public RestResponse<Nullable> unbind(@PathVariable("owner") String owner, @RequestBody List<String> roleCodes){
	    logger.info("[进入unbind controller]");
	    String[] roleCodeArray = roleCodes.toArray(new String[roleCodes.size()]);
		authorizationService.unbind(owner, roleCodeArray);
		logger.info("[退出unbind controller]");
		return RestResponse.success();
	}
	
	@ApiOperation("获取权限树")
    @GetMapping(value = "/privilegeTree/roles")
    public RestResponse<PrivilegeTreeDTO> queryPrivilegeTree(){
        PrivilegeTreeDTO privilegeTreeDTO = authorizationService.queryPrivilegeTree(null);
        return RestResponse.success(privilegeTreeDTO);
    }
	 
	@ApiOperation("根据角色编码获取权限树")
	@ApiImplicitParam(name = "roleCodes", value = "角色编码列表", required = true, dataType = "String", paramType="path")
    @GetMapping(value = "/privilegeTree/roles/{roleCodes}")
	public RestResponse<PrivilegeTreeDTO> queryPrivilegeTree(@PathVariable("roleCodes") String [] roleCodes){
		PrivilegeTreeDTO privilegeTreeDTO = authorizationService.queryPrivilegeTree(roleCodes);
		return RestResponse.success(privilegeTreeDTO);
	}
	
	 
	@ApiOperation("创建权限组")
	@ApiImplicitParam(name = "privilegeGroup", value = "权限组", required = true, dataType = "PrivilegeGroupDTO")
	@PostMapping(value = "/privilegeGroup")
	public RestResponse<PrivilegeGroupDTO> createPrivilegeGroup(@RequestBody PrivilegeGroupDTO privilegeGroup) {
	    PrivilegeGroupDTO dto = authorizationService.createPrivilegeGroup(privilegeGroup);
		return RestResponse.success(dto);
	}
	
     
	@ApiOperation("修改权限组")
	@ApiImplicitParam(name = "privilegeGroup", value = "权限组", required = true, dataType = "PrivilegeGroupDTO")
	@PutMapping(value = "/privilegeGroup")
	public RestResponse<Nullable> modifyPrivilegeGroup(@RequestBody PrivilegeGroupDTO privilegeGroup) {
		authorizationService.modifyPrivilegeGroup(privilegeGroup);
		return RestResponse.success();
	}
	
     
	@ApiOperation("删除权限组")
	@ApiImplicitParam(name = "id", value = "权限组id", required = true, dataType = "Long", paramType="path")
	@DeleteMapping(value = "/privilegeGroup/{id}")
	public RestResponse<Nullable> removePrivilegeGroup(@PathVariable("id") Long id){
		authorizationService.removePrivilegeGroup(id);
		return RestResponse.success();
	}
	
     
	@ApiOperation("根据id获取权限组")
	@ApiImplicitParam(name = "id", value = "权限组id", required = true, dataType = "Long", paramType="path")
	@GetMapping(value = "/privilegeGroup/{id}")
	public RestResponse<PrivilegeGroupDTO> queryPrivilegeGroup(@PathVariable("id") Long id){
	    PrivilegeGroupDTO dto = authorizationService.queryPrivilegeGroup(id);
		return RestResponse.success(dto);
	}
	
     
	@ApiOperation("获取所有权限组")
	@GetMapping(value = "/privilegeGroup")
	public RestResponse<List<PrivilegeGroupDTO>> queryAllPrivilegeGroup(){
	    List<PrivilegeGroupDTO> list = authorizationService.queryAllPrivilegeGroup();
		return RestResponse.success(list);
	}
	
	 
	@ApiOperation("创建权限")
	@ApiImplicitParam(name = "privilege", value = "权限", required = true, dataType = "PrivilegeDTO")
	@PostMapping(value = "/privilege")
	public RestResponse<PrivilegeDTO> createPrivilege(@RequestBody PrivilegeDTO privilege) {
		PrivilegeDTO dto = authorizationService.createPrivilege(privilege);
		return RestResponse.success(dto);
	}
	
	 
	@ApiOperation("修改权限")
	@ApiImplicitParam(name = "privilege", value = "权限", required = true, dataType = "PrivilegeDTO")
	@PutMapping(value = "/privilege")
	public RestResponse<Nullable> modifyPrivilege(@RequestBody PrivilegeDTO privilege){
		authorizationService.modifyPrivilege(privilege);
		return RestResponse.success();
	}
	
	 
	@ApiOperation("删除权限")
	@ApiImplicitParam(name = "privilegeCodes", value = "权限编码列表", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/privilege/{privilegeCodes}")
	public RestResponse<Nullable> removePrivilege(@PathVariable("privilegeCodes") String[] privilegeCodes){
		authorizationService.removePrivilege(privilegeCodes);
		return RestResponse.success();
	}
	
	 
	@ApiOperation("根据权限编码获取权限")
	@ApiImplicitParam(name = "privilegeCode", value = "权限编码", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/privilege/{privilegeCode}")
	@ResponseBody
	public RestResponse<PrivilegeDTO> queryPrivilege(@PathVariable("privilegeCode") String privilegeCode){
	    PrivilegeDTO privilege = authorizationService.queryPrivilege(privilegeCode);
		return RestResponse.success(privilege);
	}
	
	 
	@ApiOperation("分页条件检索权限")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "权限查询参数",  dataType = "PrivilegeQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "请求页码", required = true, dataType = "int", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType="query")
	})
	@PostMapping(value = "/privilege/page")
	public RestResponse<Page<PrivilegeDTO>> pagePrivilegeByConditions(@RequestBody PrivilegeQueryParams queryParams
			,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<PrivilegeDTO> page = authorizationService.pagePrivilegeByConditions(queryParams, pageNo, pageSize);
		return RestResponse.success(page);
	}
	
	 
	@ApiOperation("新创建角色并绑定权限")
	@ApiImplicitParam(name = "role", value = "角色", required = true, dataType = "RoleDTO")
	@PostMapping(value = "/role")
	public RestResponse<RoleDTO> createRole(@RequestBody RoleDTO role){
		RoleDTO dto = authorizationService.createRole(role);
		return RestResponse.success(dto);
	}
	
	 
	@ApiOperation("修改角色")
	@ApiImplicitParam(name = "role", value = "角色", required = true, dataType = "RoleDTO")
	@PutMapping(value = "/role")
	public RestResponse<Nullable> modifyRole(@RequestBody RoleDTO role){
		authorizationService.modifyRole(role);
		return RestResponse.success();
	}
	
	 
	@ApiOperation("角色绑定权限")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "privilegeCodes", value = "权限编码列表", required = true, dataTypeClass = List.class, paramType="body")
	})
	@PutMapping(value = "/role/{roleCode}/privileges/")
	public RestResponse<Nullable> roleBindPrivilege(@PathVariable("roleCode") String roleCode, @RequestBody List<String> privilegeCodes){
	    String[] pCodes = new String[privilegeCodes.size()];
	    privilegeCodes.toArray(pCodes);
		authorizationService.roleBindPrivilege(roleCode, pCodes);
		return RestResponse.success();
	}
	
	 
	@ApiOperation("删除角色")
	@ApiImplicitParam(name = "roleCodes", value = "角色编码列表", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/role/{roleCodes}")
	public RestResponse<String> removeRole(@PathVariable("roleCodes") String[] roleCodes){
		authorizationService.removeRole(roleCodes);
		return RestResponse.success("");
	}
	
	 
	@ApiOperation("根据角色编码获取角色（包含权限）")
	@ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/role/{roleCode}")
	public RestResponse<RoleDTO> queryRole(@PathVariable("roleCode") String roleCode){
	    RoleDTO role = authorizationService.queryRole(roleCode);
		return RestResponse.success(role);
	}
	
	 
	@ApiOperation("根据角色编码列表获取多个角色")
	@ApiImplicitParam(name = "roleCodes", value = "角色编码列表", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/roles/{roleCodes}")
	public RestResponse<List<RoleDTO>> queryRole(@PathVariable("roleCodes") String [] roleCodes){
	    List<RoleDTO> list = authorizationService.queryRole(roleCodes);
		return RestResponse.success(list);
	}

	 
	@ApiOperation("分页查询角色列表(不包含权限)")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "权限查询参数",  dataType = "RoleQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "请求页码", required = true, dataType = "int", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType="query")
	})
	@PostMapping(value = "/role/page")
	public RestResponse<Page<RoleDTO>> pageRoleByConditions(@RequestBody RoleQueryParams queryParams
			,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
	    Page<RoleDTO> page = authorizationService.pageRoleByConditions(queryParams, pageNo, pageSize);
		return RestResponse.success(page);
	}
}
