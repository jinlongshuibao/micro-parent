package com.uiotsoft.micro.api.user;

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

import com.uiotsoft.micro.api.user.dto.AccountDTO;
import com.uiotsoft.micro.api.user.dto.AccountQueryParams;
import com.uiotsoft.micro.api.user.dto.AuthenticationInfo;
import com.uiotsoft.micro.api.user.dto.FastAuthenticationInfo;
import com.uiotsoft.micro.api.user.dto.LoginInfoDTO;
import com.uiotsoft.micro.api.user.dto.LoginRequestDTO;
import com.uiotsoft.micro.api.user.dto.ModifyMobileParams;
import com.uiotsoft.micro.api.user.dto.ModifyPasswordParams;
import com.uiotsoft.micro.api.user.dto.TenantCreationParam;
import com.uiotsoft.micro.api.user.dto.TenantDTO;
import com.uiotsoft.micro.api.user.dto.TenantQueryParams;
import com.uiotsoft.micro.api.user.dto.TenantRoleDTO;
import com.uiotsoft.micro.api.user.dto.TenantTypeDTO;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.RestResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author 孔得峰
 * @date 2018年7月30日 上午9:47:38
 * 
 */
@FeignClient(value = "micro-user",fallback=UserRemoteHystrix.class)
public interface UserService {
    
    @PutMapping(value = "/user/account/password/noOld")
    public RestResponse<Nullable> modifyAccountPasswordNoOld(@RequestBody ModifyPasswordParams modifyPasswordParams);
    
    @PostMapping(value = "/user/tenantAndAccount")
    public RestResponse<TenantCreationParam> createTenantAndAccount(@RequestBody TenantCreationParam creationParam);
    
	@PostMapping(value = "/user/certificate")
	public RestResponse<AccountDTO> authentication(@RequestBody AuthenticationInfo authenticationInfo);

	@PostMapping(value = "/user/fastCertificate")
	public RestResponse<AccountDTO> fastAuthentication(@RequestBody FastAuthenticationInfo authenticationInfo);
	
	@PostMapping(value = "/user/session")
	public RestResponse<LoginInfoDTO> login(@RequestBody LoginRequestDTO loginRequest);
	
	@GetMapping(value = "/user/tenant/username/{username}")
	public RestResponse<List<TenantDTO>> queryTenantByUserName(@PathVariable("username") String username);
	
	@GetMapping(value = "/user/tenant/username/{username}/tenantTypeCode/{tenantTypeCode}")
	public RestResponse<List<TenantDTO>> queryTenant(@PathVariable("username")String username, @PathVariable("tenantTypeCode")String tenantTypeCode);
	
    @PostMapping(value = "/tenantTypeRole/tenantType/{tenantTypeCode}/role/{roleCode}")
    public RestResponse<Nullable> bindRoleTypeOfTT(@PathVariable("tenantTypeCode") String tenantITypeCode, @PathVariable("roleCode") String roleCode);
    
    @DeleteMapping(value = "/tenantTypeRole/tenantType/{tenantTypeCode}/role/{roleCode}")
    public RestResponse<Nullable> unbindRoleTypeOfTT(@PathVariable("tenantTypeCode") String tenantITypeCode, @PathVariable("roleCode") String roleCode);
	
	@PostMapping(value = "/user/tenantType")
	public RestResponse<TenantTypeDTO> createTenantType(@RequestBody TenantTypeDTO tenantType);
	
	@DeleteMapping(value = "/user/tenantType/{tenantTypeCode}")
	public RestResponse<Nullable>removeTenantType(@PathVariable("tenantTypeCode") String tenantTypeCode);
	
	@PutMapping(value = "/user/tenantType")
	public RestResponse<Nullable> modifyTenantType(@RequestBody TenantTypeDTO tenantType);
	
	@GetMapping(value = "/user/tenantType")
	public RestResponse<List<TenantTypeDTO>> queryAllTenantType();
	
	@GetMapping(value = "/user/tenantType/{tenantTypeCode}")
	public RestResponse<TenantTypeDTO>queryTenantType(@PathVariable("tenantTypeCode") String tenantTypeCode);
	
	@PostMapping(value = "/user/tenant")
	public RestResponse<TenantDTO> createTenant(@RequestBody TenantDTO tenant);
	
	@DeleteMapping(value = "/user/tenant/{tenantId}")
	public RestResponse<Nullable>removeTenant(@PathVariable("tenantId") Long tenantId);
	
	@PutMapping(value = "/user/tenant")
	public RestResponse<Nullable> modifyTenant(@RequestBody TenantDTO tenant);
	
	@GetMapping(value = "/user/tenant/{tenantId}")
	public RestResponse<TenantDTO> queryTenant(@PathVariable("tenantId") Long tenantId);
	
	@GetMapping(value = "/user/tenants/{tenantIds}")
	public RestResponse<List<TenantDTO>> queryTenant(@PathVariable("tenantId")Long[] tenantIds);
	
	@PostMapping(value = "/user/tenant/page")
	public RestResponse<Page<TenantDTO>> pageTenantByConditions(@RequestBody TenantQueryParams queryParams
			,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
	
	@PostMapping(value = "/user/tenant/{tenantId}/role")
	public RestResponse<Nullable> createTenantRole(@PathVariable("tenantId")Long tenantId, @RequestBody TenantRoleDTO role);
	
	@DeleteMapping(value = "/user/tenant/{tenantId}/role/{roleCode}")
	public RestResponse<Nullable> removeTenantRole(@PathVariable("tenantId")Long tenantId, @PathVariable("roleCode") String roleCode);
	
	@PutMapping(value = "/user/tenant/{tenantId}/role")
	public RestResponse<Nullable> modifyTenantRole(@PathVariable("tenantId")Long tenantId, @RequestBody TenantRoleDTO role);
	
	@GetMapping(value = "/user/tenant/{tenantId}/role")
	public RestResponse<List<TenantRoleDTO>> queryTenantRole(@PathVariable("tenantId")Long tenantId);
	
	@GetMapping(value = "/user/tenant/{tenantId}/role/{roleCode}")
	public RestResponse<TenantRoleDTO> queryTenantRole(@PathVariable("tenantId")Long tenantId, @PathVariable("roleCode")String roleCode);
	
	@PostMapping(value = "/user/account")
	public RestResponse<AccountDTO> createAccount(@RequestBody AccountDTO account);
	
	@DeleteMapping(value = "/user/account/{username}")
	public RestResponse<Nullable>removeAccount(@PathVariable("username") String username);
	
	@PutMapping(value = "/user/account/password")
	public RestResponse<Nullable> modifyAccountPassword(@RequestBody ModifyPasswordParams modifyPasswordParams);
	
	@PutMapping(value = "/user/account/mobile")
	public RestResponse<Nullable> modifyAccountMobile(@RequestBody ModifyMobileParams modifyMobileParams);
	
	@GetMapping(value = "/user/account/{username}")
	public RestResponse<AccountDTO> queryAccount(@PathVariable("username") String username);
	
	@GetMapping(value = "/user/account/mobile/{mobile}")
	public RestResponse<List<AccountDTO>> queryAccountByMobile(@PathVariable("mobile") String mobile);
	
	@GetMapping(value = "/user/accounts/{usernames}")
	public RestResponse<List<AccountDTO>> queryAccount(@PathVariable("usernames") String [] usernames);
	
	@PostMapping(value = "/user/account/page")
	public RestResponse<Page<AccountDTO>> pageAccountByConditions(@RequestBody AccountQueryParams queryParams
			,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
	
	@GetMapping(value = "/user/username/{username}")
	public RestResponse<Boolean> isExistedUsername(@PathVariable("username") String username);
}

@Component
class UserRemoteHystrix implements UserService{
	
    @Override
    public RestResponse<TenantCreationParam> createTenantAndAccount(TenantCreationParam creationParam) {
        return new RestResponse<TenantCreationParam>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
    }
    
	@Override
	public RestResponse<AccountDTO> authentication(AuthenticationInfo authenticationInfo) {
		return new RestResponse<AccountDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<AccountDTO> fastAuthentication(FastAuthenticationInfo authenticationInfo) {
		return new RestResponse<AccountDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<LoginInfoDTO> login(LoginRequestDTO loginRequest) {
		return new RestResponse<LoginInfoDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<TenantDTO>> queryTenantByUserName(String username) {
		return new RestResponse<List<TenantDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<TenantDTO>> queryTenant(String username, String tenantTypeCode) {
		return new RestResponse<List<TenantDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<TenantTypeDTO> createTenantType(TenantTypeDTO tenantType) {
		return new RestResponse<TenantTypeDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> removeTenantType(String tenantTypeCode) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyTenantType(TenantTypeDTO tenantType) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<TenantTypeDTO>> queryAllTenantType() {
		return new RestResponse<List<TenantTypeDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<TenantTypeDTO> queryTenantType(String tenantTypeCode) {
		return new RestResponse<TenantTypeDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<TenantDTO> createTenant(TenantDTO tenant) {
		return new RestResponse<TenantDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> removeTenant(Long tenantId) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyTenant(TenantDTO tenant) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<TenantDTO> queryTenant(Long tenantId) {
		return new RestResponse<TenantDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<TenantDTO>> queryTenant(Long[] tenantIds) {
		return new RestResponse<List<TenantDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Page<TenantDTO>> pageTenantByConditions(TenantQueryParams queryParams, Integer pageNo,
			Integer pageSize) {
		return new RestResponse<Page<TenantDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> createTenantRole(Long tenantId, TenantRoleDTO role) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> removeTenantRole(Long tenantId, String roleCode) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyTenantRole(Long tenantId, TenantRoleDTO role) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<TenantRoleDTO>> queryTenantRole(Long tenantId) {
		return new RestResponse<List<TenantRoleDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<TenantRoleDTO> queryTenantRole(Long tenantId, String roleCode) {
		return new RestResponse<TenantRoleDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<AccountDTO> createAccount(AccountDTO account) {
		return new RestResponse<AccountDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> removeAccount(String username) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyAccountPassword(ModifyPasswordParams modifyPasswordParams) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyAccountMobile(ModifyMobileParams modifyMobileParams) {
		return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<AccountDTO> queryAccount(String username) {
		return new RestResponse<AccountDTO>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<AccountDTO>> queryAccountByMobile(String mobile) {
		return new RestResponse<List<AccountDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<List<AccountDTO>> queryAccount(String[] usernames) {
		return new RestResponse<List<AccountDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Page<AccountDTO>> pageAccountByConditions(AccountQueryParams queryParams, Integer pageNo,
			Integer pageSize) {
		return new RestResponse<Page<AccountDTO>>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

	@Override
	public RestResponse<Boolean> isExistedUsername(String username) {
		return new RestResponse<Boolean>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
	}

    @Override
    public RestResponse<Nullable> bindRoleTypeOfTT(String tenantITypeCode, String roleCode) {
        return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
    }

    @Override
    public RestResponse<Nullable> unbindRoleTypeOfTT(String tenantITypeCode, String roleCode) {
        return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
    }

    @Override
    public RestResponse<Nullable> modifyAccountPasswordNoOld(ModifyPasswordParams modifyPasswordParams) {
        return new RestResponse<Nullable>(ErrorCode.E_999992.getCode(), ErrorCode.E_999992.getDesc());
    }
	
}
