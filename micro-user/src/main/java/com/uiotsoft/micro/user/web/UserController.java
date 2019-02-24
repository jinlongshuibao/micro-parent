package com.uiotsoft.micro.user.web;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.uiotsoft.micro.common.domain.RestResponse;
import com.uiotsoft.micro.user.dto.AccountDTO;
import com.uiotsoft.micro.user.dto.AccountQueryParams;
import com.uiotsoft.micro.user.dto.AuthenticationInfo;
import com.uiotsoft.micro.user.dto.FastAuthenticationInfo;
import com.uiotsoft.micro.user.dto.LoginInfoDTO;
import com.uiotsoft.micro.user.dto.LoginRequestDTO;
import com.uiotsoft.micro.user.dto.ModifyMobileParams;
import com.uiotsoft.micro.user.dto.ModifyPasswordParams;
import com.uiotsoft.micro.user.dto.TenantCreationParam;
import com.uiotsoft.micro.user.dto.TenantDTO;
import com.uiotsoft.micro.user.dto.TenantQueryParams;
import com.uiotsoft.micro.user.dto.TenantRoleDTO;
import com.uiotsoft.micro.user.dto.TenantTypeDTO;
import com.uiotsoft.micro.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
@Api(value = "用户服务", tags = "UserApi", description="包含租户类型、租户、租户下角色、账号等信息维护")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation("创建租户并创建账号及其关系")
	@ApiImplicitParam(name = "creationParam", value = "租户和账号信息", required = true, dataType = "TenantCreationParam", paramType="body")
    @PostMapping(value = "/tenantAndAccount")
    public RestResponse<TenantCreationParam> createTenantAndAccount(@RequestBody TenantCreationParam creationParam){
	    TenantCreationParam tenant = userService.createTenantAndAccount(creationParam);
        RestResponse<TenantCreationParam> response = RestResponse.success(tenant);
        if(tenant.isAccountExist()){
            response.setMsg("账号已经存在，所以密码仍为原始密码");
        }else{
            response.setMsg("创建成功");
        }
        return response;
    }
	
	@ApiOperation("用户认证")
	@ApiImplicitParam(name = "authenticationInfo", value = "认证参数", required = true, dataType = "AuthenticationInfo", paramType="body")
	@PostMapping(value = "/certificate")
	public RestResponse<AccountDTO> authentication(@RequestBody AuthenticationInfo authenticationInfo){
		AccountDTO dto = userService.authentication(authenticationInfo);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("用户快捷认证")
	@ApiImplicitParam(name = "authenticationInfo", value = "认证参数", required = true, dataType = "FastAuthenticationInfo", paramType="body")
	@PostMapping(value = "/fastCertificate")
	public RestResponse<AccountDTO> fastAuthentication(@RequestBody FastAuthenticationInfo authenticationInfo){
		AccountDTO dto = userService.fastAuthentication(authenticationInfo);
		return RestResponse.success(dto);
	}
	
	
	@ApiOperation("用户登录租户")
	@ApiImplicitParam(name = "loginRequest", value = "登录租户参数", required = true, dataType = "LoginRequestDTO", paramType="body")
	@PostMapping(value = "/session")
	public RestResponse<LoginInfoDTO> login(@RequestBody LoginRequestDTO loginRequest){
		LoginInfoDTO dto = userService.login(loginRequest);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("查询用户所属租户列表")
	@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/tenant/username/{username}")
	public RestResponse<List<TenantDTO>> queryTenantByUserName(@PathVariable("username") String username){
		List<TenantDTO> dtoList = userService.queryTenantByUserName(username);
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation("查询用户所属某租户类型租户列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "tenantTypeCode", value = "租户类型编码", required = true, dataType = "String", paramType="path")
	})
	@GetMapping(value = "/tenant/username/{username}/tenantTypeCode/{tenantTypeCode}")
	public RestResponse<List<TenantDTO>> queryTenant(@PathVariable("username")String username, @PathVariable("tenantTypeCode")String tenantTypeCode){
		List<TenantDTO> dtoList = userService.queryTenant(username, tenantTypeCode);
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation("绑定租户类型和角色关系")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "tenantTypeCode", value = "租户类型编码", required = true, dataType = "String", paramType="path"),
    	@ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType="path")
	})
    @PostMapping(value = "/tenantTypeRole/tenantType/{tenantTypeCode}/role/{roleCode}")
    public RestResponse<Nullable> bindRoleTypeOfTT(@PathVariable("tenantTypeCode") String tenantITypeCode, @PathVariable("roleCode") String roleCode){
        userService.bindRoleTypeOfTT(tenantITypeCode,  roleCode);
        return RestResponse.success();
    }
	
	@ApiOperation("解绑租户类型和角色关系")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "tenantTypeCode", value = "租户类型编码", required = true, dataType = "String", paramType="path"),
        @ApiImplicitParam(name = "roleCode", value = "角色编码", required = true, dataType = "String", paramType="path")
    })
    @DeleteMapping(value = "/tenantTypeRole/tenantType/{tenantTypeCode}/role/{roleCode}")
    public RestResponse<Nullable> unbindRoleTypeOfTT(@PathVariable("tenantTypeCode") String tenantITypeCode, @PathVariable("roleCode") String roleCode){
        userService.unbindRoleTypeOfTT(tenantITypeCode,  roleCode);
        return RestResponse.success();
    }
	
	@ApiOperation("创建租户类型")
	@ApiImplicitParam(name = "tenantType", value = "租户类型", required = true, dataType = "TenantTypeDTO", paramType="body")
	@PostMapping(value = "/tenantType")
	public RestResponse<TenantTypeDTO> createTenantType(@RequestBody TenantTypeDTO tenantType){
		TenantTypeDTO tenantTypeResult = userService.createTenantType(tenantType);
		return RestResponse.success(tenantTypeResult);
	}
	
	@ApiOperation("删除租户类型")
	@ApiImplicitParam(name = "tenantTypeCode", value = "租户类型编码", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/tenantType/{tenantTypeCode}")
	public RestResponse<Nullable>removeTenantType(@PathVariable("tenantTypeCode") String tenantTypeCode){
		userService.removeTenantType(tenantTypeCode);
		return RestResponse.success();
	}
	
	@ApiOperation("修改租户类型")
	@ApiImplicitParam(name = "tenantType", value = "租户类型", required = true, dataType = "TenantTypeDTO", paramType="body")
	@PutMapping(value = "/tenantType")
	public RestResponse<Nullable> modifyTenantType(@RequestBody TenantTypeDTO tenantType){
		userService.modifyTenantType(tenantType);
		return RestResponse.success();
	}
	
	@ApiOperation("获取所有租户类型")
	@GetMapping(value = "/tenantType")
	public RestResponse<List<TenantTypeDTO>> queryAllTenantType(){
		List<TenantTypeDTO> dtoList = userService.queryAllTenantType();
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation("查询某租户类型（包含该租户类型下的角色code）")
	@ApiImplicitParam(name = "tenantTypeCode", value = "租户类型编码", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/tenantType/{tenantTypeCode}")
	public RestResponse<TenantTypeDTO>queryTenantType(@PathVariable("tenantTypeCode") String tenantTypeCode){
		TenantTypeDTO dto = userService.queryTenantType(tenantTypeCode);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("创建租户")
	@ApiImplicitParam(name = "tenant", value = "租户", required = true, dataType = "TenantDTO", paramType="body")
	@PostMapping(value = "/tenant")
	public RestResponse<TenantDTO> createTenant(@RequestBody TenantDTO tenant){
		TenantDTO tenantResult = userService.createTenant(tenant);
		return RestResponse.success(tenantResult);
	}
	
	@ApiOperation("删除租户")
	@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/tenant/{tenantId}")
	public RestResponse<Nullable>removeTenant(@PathVariable("tenantId") Long tenantId){
		userService.removeTenant(tenantId);
		return RestResponse.success();
	}
	
	@ApiOperation("修改租户")
	@ApiImplicitParam(name = "tenant", value = "租户", required = true, dataType = "TenantDTO", paramType="body")
	@PutMapping(value = "/tenant")
	public RestResponse<Nullable> modifyTenant(@RequestBody TenantDTO tenant){
		userService.modifyTenant(tenant);
		return RestResponse.success();
	}
	
	@ApiOperation("查询单个租户")
	@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/tenant/{tenantId}")
	public RestResponse<TenantDTO> queryTenant(@PathVariable("tenantId") Long tenantId){
		TenantDTO dto = userService.queryTenant(tenantId);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("查询多个租户")
	@ApiImplicitParam(name = "tenantIds", value = "租户id列表", required = true, paramType="path")
	@GetMapping(value = "/tenants/{tenantIds}")
	public RestResponse<List<TenantDTO>> queryTenant(@PathVariable("tenantIds")Long[] tenantIds){
		List<TenantDTO> dtoList = userService.queryTenant(tenantIds);
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation("分页条件检索租户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "租户查询参数",  dataType = "TenantQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "请求页码", required = true, dataType = "String", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "String", paramType="query")
	})
	@PostMapping(value = "/tenant/page")
	public RestResponse<Page<TenantDTO>> pageTenantByConditions(@RequestBody TenantQueryParams queryParams
			,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<TenantDTO> pageDto = userService.pageTenantByConditions(queryParams, pageNo, pageSize);
		return RestResponse.success(pageDto);
	}
	
	@ApiOperation("创建租户内角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "role", value = "租户内角色", required = true, dataType = "TenantRoleDTO", paramType="body")
	})
	@PostMapping(value = "/tenant/{tenantId}/role")
	public RestResponse<Nullable> createTenantRole(@PathVariable("tenantId")Long tenantId, @RequestBody TenantRoleDTO role){
		userService.createTenantRole(tenantId, role);
		return RestResponse.success();
	}
	
	@ApiOperation("删除租户内角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "roleCode", value = "租户内角色编码", required = true, dataType = "String", paramType="path")
	})
	@DeleteMapping(value = "/tenant/{tenantId}/role/{roleCode}")
	public RestResponse<Nullable> removeTenantRole(@PathVariable("tenantId")Long tenantId, @PathVariable("roleCode") String roleCode){
		userService.removeTenantRole(tenantId, roleCode);
		return RestResponse.success();
	}
	
	@ApiOperation("修改租户内角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "role", value = "租户内角色", required = true, dataType = "TenantRoleDTO", paramType="body")
	})
	@PutMapping(value = "/tenant/{tenantId}/role")
	public RestResponse<Nullable> modifyTenantRole(@PathVariable("tenantId")Long tenantId, @RequestBody TenantRoleDTO role){
		userService.modifyTenantRole(tenantId, role);
		return RestResponse.success();
	}
	
	@ApiOperation(value="查询租户内角色列表")
	@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/tenant/{tenantId}/role")
	public RestResponse<List<TenantRoleDTO>> queryTenantRole(@PathVariable("tenantId")Long tenantId){
		List<TenantRoleDTO> dtoList = userService.queryTenantRole(tenantId);
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation(value="查询租户内角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "roleCode", value = "租户内角色编码", required = true, dataType = "String", paramType="path")
	})
	@GetMapping(value = "/tenant/{tenantId}/role/{roleCode}")
	public RestResponse<TenantRoleDTO> queryTenantRole(@PathVariable("tenantId")Long tenantId, @PathVariable("roleCode")String roleCode){
		TenantRoleDTO dto = userService.queryTenantRole(tenantId, roleCode);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("创建账号")
	@ApiImplicitParam(name = "account", value = "账号信息", required = true, dataType = "AccountDTO", paramType="body")
	@PostMapping(value = "/account")
	public RestResponse<AccountDTO> createAccount(@RequestBody AccountDTO account){
		userService.createAccount(account);
		return RestResponse.success(account);
	}
	
	@ApiOperation("账号绑定租户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "int", paramType="path")
	})
	@PutMapping(value = "/bind/account/{username}/tenant/{tenantId}")
	public RestResponse<Nullable> accountBindTenant(@PathVariable("username")String username, @PathVariable("tenantId")String tenantId){
		userService.bind(username, tenantId);
		return RestResponse.success();
	}
	
	@ApiOperation("删除账号")
	@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/account/{username}")
	public RestResponse<Nullable>removeAccount(@PathVariable("username") String username){
		userService.removeAccount(username);
		return RestResponse.success();
	}
	
	@ApiOperation("账号解绑租户")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path"),
		@ApiImplicitParam(name = "tenantId", value = "租户id", required = true, dataType = "String", paramType="path")
	})
	@DeleteMapping(value = "/unbind/account/{username}/tenant/{tenantId}")
	public RestResponse<Nullable> accountUnbindTenant(@PathVariable("username")String username, @PathVariable("tenantId")String tenantId){
		userService.unbind(username, tenantId);
		return RestResponse.success();
	}
	
	@ApiOperation("修改密码")
	@ApiImplicitParam(name = "modifyPasswordParams", value = "修改密码参数", required = true, dataType = "ModifyPasswordParams", paramType="body")
	@PutMapping(value = "/account/password")
	public RestResponse<Nullable> modifyAccountPassword(@RequestBody ModifyPasswordParams modifyPasswordParams){
		userService.modifyAccountPassword(modifyPasswordParams);
		return RestResponse.success();
	}
	
	@ApiOperation("修改密码无需旧密码")
	@ApiImplicitParam(name = "modifyPasswordParams", value = "修改密码参数", required = true, dataType = "ModifyPasswordParams", paramType="body")
	@PutMapping(value = "/account/password/noOld")
	public RestResponse<Nullable> modifyAccountPasswordNoOld(@RequestBody ModifyPasswordParams modifyPasswordParams){
		userService.modifyAccountPasswordNoOld(modifyPasswordParams);
		return RestResponse.success();
	}
	
	@ApiOperation("找回密码")
	@ApiImplicitParam(name = "modifyPasswordParams", value = "找回密码参数", required = true, dataType = "ModifyPasswordParams", paramType="body")
	@PutMapping(value = "/account/findPassword")
	public RestResponse<Nullable> findAccountPassword(@RequestBody ModifyPasswordParams modifyPasswordParams){
		userService.findAccountPassword(modifyPasswordParams);
		return RestResponse.success();
	}
	
	@ApiOperation("修改手机号")
	@ApiImplicitParam(name = "modifyMobileParams", value = "修改手机号参数", required = true, dataType = "ModifyMobileParams", paramType="body")
	@PutMapping(value = "/account/mobile")
	public RestResponse<Nullable> modifyAccountMobile(@RequestBody ModifyMobileParams modifyMobileParams){
		userService.modifyAccountMobile(modifyMobileParams);
		return RestResponse.success();
	}
	
	@ApiOperation("根据用户名查询账号")
	@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/account/{username}")
	public RestResponse<AccountDTO> queryAccount(@PathVariable("username") String username){
		AccountDTO dto = userService.queryAccount(username);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("根据手机号查询账号")
	@ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/account/mobile/{mobile}")
	public RestResponse<List<AccountDTO>> queryAccountByMobile(@PathVariable("mobile") String mobile){
		List<AccountDTO> dto = userService.queryAccountByMobile(mobile);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("查询多个账号")
	@ApiImplicitParam(name = "usernames", value = "用户名列表", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/accounts/{usernames}")
	public RestResponse<List<AccountDTO>> queryAccount(@PathVariable("usernames") String [] usernames){
		List<AccountDTO> dtoList = userService.queryAccount(usernames); 
		return RestResponse.success(dtoList);
	}
	
	@ApiOperation("分页条件检索账号")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "账号查询参数",  dataType = "AccountQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "请求页码", required = true, dataType = "String", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "String", paramType="query")
	})
	@PostMapping(value = "/account/page")
	public RestResponse<Page<AccountDTO>> pageAccountByConditions(@RequestBody AccountQueryParams queryParams
			,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<AccountDTO> page = userService.pageAccountByConditions(queryParams, pageNo, pageSize);
		return RestResponse.success(page);
	}
	
	@ApiOperation("账号是否存在")
	@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/username/{username}")
	public RestResponse<Boolean> isExistedUsername(@PathVariable("username") String username){
		boolean result = userService.isExistedUsername(username);
		return RestResponse.success(result);
	}
	
	

}
