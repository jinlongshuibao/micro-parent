package com.uiotsoft.micro.user.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uiotsoft.micro.api.authorization.AuthorizationService;
import com.uiotsoft.micro.api.authorization.dto.AuthorizationInfoDTO;
import com.uiotsoft.micro.api.authorization.dto.PrivilegeDTO;
import com.uiotsoft.micro.api.authorization.dto.RoleDTO;
import com.uiotsoft.micro.common.domain.BusinessException;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.common.domain.RestResponse;
import com.uiotsoft.micro.common.util.VerifyUtil;
import com.uiotsoft.micro.user.dao.AccountDao;
import com.uiotsoft.micro.user.dao.TenantDao;
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
import com.uiotsoft.micro.user.dto.TenantTypeRoleDTO;
import com.uiotsoft.micro.user.entity.Account;
import com.uiotsoft.micro.user.entity.AccountTenant;
import com.uiotsoft.micro.user.entity.Tenant;
import com.uiotsoft.micro.user.entity.TenantRole;
import com.uiotsoft.micro.user.entity.TenantType;
import com.uiotsoft.micro.user.entity.TenantTypeRole;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private TenantDao tenantDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AuthorizationService authService;
	
	@Override
	@Transactional
	public TenantCreationParam createTenantAndAccount(TenantCreationParam tenantCreationParam) {
	    TenantCreationParam resultTenant = new TenantCreationParam();
	    
	    //参数校验
        String name = tenantCreationParam.getName();
        if(StringUtils.isBlank(name)){
            logger.info("租户名称为空");
            throw new BusinessException(ErrorCode.E_130111);
        }
        if(!VerifyUtil.verifyLength(name, 50)){
            throw new BusinessException(ErrorCode.E_130131);
        }
        
        String tenantTypeCode = tenantCreationParam.getTenantTypeCode();
        if(StringUtils.isBlank(tenantTypeCode)){
            logger.info("租户类型编码为空");
            throw new BusinessException(ErrorCode.E_130110);
        }
        if(!VerifyUtil.verifyLength(tenantTypeCode, 50)){
            throw new BusinessException(ErrorCode.E_130133);
        }
        if(!VerifyUtil.verifyCharacter(tenantTypeCode)){
            throw new BusinessException(ErrorCode.E_130132);
        }    
        
        String username = tenantCreationParam.getUsername();
        if(StringUtils.isBlank(username)){
            logger.info("账号为空");
            throw new BusinessException(ErrorCode.E_130101);
        }
        if(!VerifyUtil.verifyLength(username, 50)){
            throw new BusinessException(ErrorCode.E_130135);
        }
        if(!VerifyUtil.verifyCharacter(username)){
            throw new BusinessException(ErrorCode.E_130134);
        }
        
        String password = tenantCreationParam.getPassword();
        //判断用户输入的账号是否已经存在
        boolean existedUsername = isExistedUsername(username); 
        if(!existedUsername){//如果账号不存在，则需要对手机号和密码进行非空校验，以便创建账号
            if(StringUtils.isBlank(password)){
                logger.info("密码为空");
                throw new BusinessException(ErrorCode.E_120101);
            }
            String phoneNum = tenantCreationParam.getPhoneNum();
            if(StringUtils.isBlank(phoneNum)){
                logger.info("手机号为空");
                throw new BusinessException(ErrorCode.E_130105);
            }
            if(!VerifyUtil.verifyPhoneNum(phoneNum)){
                throw new BusinessException(ErrorCode.E_130136);
            }
        }
        
        /*
         * 1.创建租户
         */
        TenantDTO tenantDto = new TenantDTO();
        tenantDto.setName(tenantCreationParam.getName());
        tenantDto.setTenantTypeCode(tenantCreationParam.getTenantTypeCode());
        TenantDTO newTenant = createTenant(tenantDto);
        tenantDto.setId(newTenant.getId());
        logger.info("[租户创建成功]{}", newTenant);
        
        /*
         * 2.新建账号(如果不存在)，并绑定账号和租户关系
         */
        if(existedUsername){//账号存在，直接绑定关系
            bind(tenantCreationParam.getUsername(), String.valueOf(newTenant.getId()));
            logger.info("[账号存在，绑定租户成功]");
        }else{
            AccountDTO account = new AccountDTO();
            //添加账号信息
            account.setUsername(tenantCreationParam.getUsername());
            account.setPassword(password);
            account.setMobile(tenantCreationParam.getPhoneNum());
            //添加账号的租户信息
            List<TenantDTO> tenants = new ArrayList<TenantDTO>();
            TenantDTO tempTenant = new TenantDTO();
            tempTenant.setId(newTenant.getId());
            tenants.add(tempTenant);
            account.setTenants(tenants);
            createAccount(account);
            logger.info("[账号不存在，创建账号并绑定租户成功]");
        }
	    
        /*
         * 3.绑定账号和租户内角色关系（绑定所有角色）
         */
        //获取租户下角色
        List<TenantRoleDTO> rolesOfTenant = queryTenantRole(newTenant.getId());
        if(rolesOfTenant == null || rolesOfTenant.isEmpty()){
            logger.info("[租户内角色为空]");
            throw new BusinessException(ErrorCode.E_130139);
        }
        List<String> roleCodes = new ArrayList<>();
        for(TenantRoleDTO dto : rolesOfTenant){
            roleCodes.add(dto.getCode());
        }
        //账号绑定租户内角色
        String owner = String.format("%s_%s", tenantCreationParam.getUsername(), newTenant.getId());
        RestResponse<Nullable> restResponse = authService.bind(owner, roleCodes);
        if(restResponse.getCode() == 0){
            logger.info("[账号绑定租户内角色失败]{}", restResponse.getMsg());
            throw new BusinessException(ErrorCode.CUSTOM, restResponse.getMsg());
        }
        
        //将新建的租户的信息（包含id）存入resultTenant
        resultTenant.setId(tenantDto.getId());
        resultTenant.setName(tenantDto.getName());
        resultTenant.setTenantTypeCode(tenantDto.getTenantTypeCode());
        //将账号写入resultTenant
        resultTenant.setUsername(username);
        //将账号是否存在写入resultTenant
        resultTenant.setAccountExist(existedUsername);
        //判断并写入手机号
        if(existedUsername){
            //查出账号的手机号并存储
            AccountDTO accountDTO = queryAccount(username);
            resultTenant.setPhoneNum(accountDTO.getMobile());
        }else{
            resultTenant.setPhoneNum(tenantCreationParam.getPhoneNum());
        }
        
	    return resultTenant;
	}
	
	@Override
	public AccountDTO authentication(AuthenticationInfo authenticationInfo) {
		if(StringUtils.isBlank(authenticationInfo.getUsername())) {
		    logger.info("[用户名为空]");
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(authenticationInfo.getPassword())) {
			throw new BusinessException(ErrorCode.E_130102);
		}
		//验证账号是否存在
		Account account = accountDao.selectAccountByUsername(authenticationInfo.getUsername());
		if(account == null) {
			throw new BusinessException(ErrorCode.E_130103);
		}
		//验证密码正确性
		if(!account.getPassword().equals(authenticationInfo.getPassword())) {
			throw new BusinessException(ErrorCode.E_130104);
		}
		
		AccountDTO dto = AccountDTO.convertEntityToDto(account);
		List<Tenant> tenants = accountDao.selectTenantsByAccountId(account.getId());
		dto.setTenants(TenantDTO.convertEntitysToDtos(tenants));
		return dto;
	}

	@Override
	public AccountDTO fastAuthentication(FastAuthenticationInfo authenticationInfo) {
		if(StringUtils.isBlank(authenticationInfo.getMobile())) {
			throw new BusinessException(ErrorCode.E_130105);
		}
		List<AccountDTO> accounts = queryAccountByMobile(authenticationInfo.getMobile());
		if(accounts == null || accounts.size() == 0) {
			throw new BusinessException(ErrorCode.E_130103);
		}else if(accounts.size() > 1){
		    throw new BusinessException(ErrorCode.E_130141);
		}
		//TODO 获取快捷登录认证码
		String code = "666666";
		if(StringUtils.isBlank(code)) {
			throw new BusinessException(ErrorCode.E_130106);
		}
		if(code.equals(authenticationInfo.getCode())) {
			throw new BusinessException(ErrorCode.E_130118);
		}
		return accounts.get(0);
	}

	@Override
	public LoginInfoDTO login(LoginRequestDTO loginRequest) {
		if(StringUtils.isBlank(loginRequest.getUsername())) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(loginRequest.getTenantId())) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		LoginInfoDTO dto = new LoginInfoDTO();
		Account account = accountDao.selectAccountByUsername(loginRequest.getUsername());
		if(account == null) {
			throw new BusinessException(ErrorCode.E_130103);
		}
		dto.setUsername(account.getUsername());
		dto.setMobile(account.getMobile());
		Tenant tenant = tenantDao.selectTenantById(Long.valueOf(loginRequest.getTenantId()));
		if(tenant == null){
		    throw new BusinessException(ErrorCode.E_130137);
		}
		dto.setTenantId(tenant.getId());
		dto.setTenantName(tenant.getName());
		dto.setTenantType(tenant.getTenantTypeCode());
		String owner = String.format("%s_%s", loginRequest.getUsername(), loginRequest.getTenantId());
		RestResponse<AuthorizationInfoDTO> response = authService.authorize(owner);
		logger.info("[调用授权服务结果]{}",response);
		if(response.getCode() == 0){
		    dto.setRolePrivilegeMap(response.getResult().getRolePrivilegeMap());
		}
		logger.info("[登陆信息]{}", dto);
		logger.info("[登陆权限]{}", dto.getRolePrivilegeMap());
		return dto;
	}

	@Override
	public List<TenantDTO> queryTenantByUserName(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		List<Tenant> list = tenantDao.selectTenantByUsername(username);
		List<TenantDTO> dtoList = TenantDTO.convertEntitysToDtos(list);
		return dtoList;
	}

	@Override
	public List<TenantDTO> queryTenant(String username, String tenantTypeCode) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(tenantTypeCode)) {
			throw new BusinessException(ErrorCode.E_130107);
		}
		List<Tenant> list = tenantDao.selectTenantByUsernameAndTenantType(username, tenantTypeCode);
		List<TenantDTO> dtoList = TenantDTO.convertEntitysToDtos(list);
		return dtoList;
	}

	@Transactional
	@Override
	public TenantTypeDTO createTenantType(TenantTypeDTO tenantType) {
		if(tenantType == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(tenantType.getName()) ) {
			throw new BusinessException(ErrorCode.E_130107);
		}
		if(!VerifyUtil.verifyLength(tenantType.getName(), 50)){
            throw new BusinessException(ErrorCode.E_130142);
        }
		if(StringUtils.isBlank(tenantType.getCode()) ) {
			throw new BusinessException(ErrorCode.E_130110);
		}
		if(!VerifyUtil.verifyLength(tenantType.getCode(), 50)){
            throw new BusinessException(ErrorCode.E_130133);
        }
        if(!VerifyUtil.verifyCharacter(tenantType.getCode())){
            throw new BusinessException(ErrorCode.E_130132);
        }   
        //去除首尾空格
		tenantType.setName(tenantType.getName().trim());
		tenantType.setCode(tenantType.getCode().trim());
		
		TenantType tenantTypeByCode = tenantDao.selectTenantTypeByCode(tenantType.getCode());
		if(tenantTypeByCode != null){
		    throw new BusinessException(ErrorCode.E_130126);
		}
		TenantType tenantTypeEntity = TenantTypeDTO.convertDtoToEntity(tenantType);
		tenantDao.insertTenantType(tenantTypeEntity);
		
		TenantType tenantTypeResult = tenantDao.selectTenantTypeById(tenantTypeEntity.getId());
		//如果有角色，则进行角色绑定
		List<TenantTypeRoleDTO> roles = tenantType.getRoles();
		if(roles!=null && roles.size()>0) {
		    for(TenantTypeRoleDTO role : roles){
		        //调用授权服务接口新增角色（添加角色，给角色授权）
	            RoleDTO roleDTO = new RoleDTO();
	            roleDTO.setName(role.getRoleName());
	            roleDTO.setCode(role.getRoleCode());
	            List<PrivilegeDTO> privilegeList = new ArrayList<>();
	            List<String> privilegeCodes = role.getPrivilegeCodes();
	            for(String privilege : privilegeCodes) {
	                PrivilegeDTO dto = new PrivilegeDTO();
	                dto.setCode(privilege);
	                privilegeList.add(dto);
	            }
	            roleDTO.setPrivileges(privilegeList);
	            
	            RestResponse<RoleDTO> newRole = authService.createRole(roleDTO);
	            if(newRole.getCode() == 120135){
	                throw new BusinessException(ErrorCode.CUSTOM, "角色编码已存在[" + role.getRoleCode() + "]" );
	            }else if(newRole.getCode() != 0){
	                throw new BusinessException(ErrorCode.CUSTOM, newRole.getMsg() );
	            }
		    }
		    
		    //增加租户类型和角色code关系
			tenantDao.insertTenantRoles(tenantType, roles);
		}
		
		return TenantTypeDTO.convertEntityToDto(tenantTypeResult); 
	}

	@Transactional
	@Override
	public void removeTenantType(String tenantTypeCode) {
		if(StringUtils.isBlank(tenantTypeCode)) {
			throw new BusinessException(ErrorCode.E_130110);
		}
		//判段租户code是否存在
		TenantType tenant = tenantDao.selectTenantTypeByCode(tenantTypeCode);
		if(tenant == null){
		    throw new BusinessException(ErrorCode.E_130128);
		}
		/*
		 * 判断租户内是否有角色，有则不允许删除
		 */
		List<TenantTypeRole> tenantTypeRoles = tenantDao.selectTenantTypeRoles(tenantTypeCode);
		if(tenantTypeRoles == null || tenantTypeRoles.size() != 0){
		    logger.info("租户类型内有角色，删除失败");
            throw new BusinessException(ErrorCode.E_130119);
        }
		/*
		 * 判断租户类型是否绑定有实体租户，有则不允许删除
		 */
		List<Tenant> tenantList = tenantDao.selectTenantByTenantType(tenantTypeCode);
		if(tenantList == null || tenantList.size() != 0){
		    logger.info("租户类型下有实体租户，操作失败");
            throw new BusinessException(ErrorCode.E_130122);
		}
		tenantDao.deleteTenantType(tenantTypeCode);
		logger.info("删除成功");
	}

	@Transactional
	@Override
	public void modifyTenantType(TenantTypeDTO tenantType) {
		if(tenantType == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(tenantType.getName())) {
			throw new BusinessException(ErrorCode.E_130107);
		}
		if(StringUtils.isBlank(tenantType.getCode())) {
			throw new BusinessException(ErrorCode.E_130110);
		} 
        //去除首尾空格
        tenantType.setName(tenantType.getName().trim());
        tenantType.setCode(tenantType.getCode().trim());
        //判段租户code是否存在
        TenantType tenant = tenantDao.selectTenantTypeByCode(tenantType.getCode());
        if(tenant == null){
            throw new BusinessException(ErrorCode.E_130128);
        }
		tenantDao.updateTenantType(tenantType);
//		tenantDao.deleteTenantTypeRoles(tenantType.getCode());
//		List<TenantTypeRoleDTO> roles = tenantType.getRoles();
//		if(roles!=null && roles.size()>0) {
//			tenantDao.insertTenantRoles(tenantType, roles);
//		}
	}

	@Override
	public List<TenantTypeDTO> queryAllTenantType() {
		List<TenantType> list = tenantDao.selectTenantTypes();
		if(list == null || list.size() == 0){
		    logger.info("查询结果为空");
		    throw new BusinessException(ErrorCode.E_130123);
		}
		return TenantTypeDTO.convertEntityToDto(list);
	}

	@Override
	public TenantTypeDTO queryTenantType(String tenantTypeCode) {
		if(StringUtils.isBlank(tenantTypeCode)){
	        throw new BusinessException(ErrorCode.E_130110);
	    }
		TenantType tenantType = tenantDao.selectTenantTypeByCode(tenantTypeCode);
		List<TenantTypeRole> roles = tenantDao.selectTenantTypeRoles(tenantTypeCode);
		TenantTypeDTO dto = TenantTypeDTO.convertEntityToDto(tenantType);
		dto.setRoles(TenantTypeRoleDTO.convertEntityToDto(roles));
		return dto;
	}

	@Override
	public void bindRoleTypeOfTT(String tenantTypeCode, String roleCode){
	    if(StringUtils.isBlank(tenantTypeCode)){
	        throw new BusinessException(ErrorCode.E_130110);
	    }
	    if(StringUtils.isBlank(roleCode)){
	        throw new BusinessException(ErrorCode.E_130125); 
	    }
	    tenantDao.bindRoleTypeOfTT(tenantTypeCode, roleCode);
	}
	
	@Override
	public void unbindRoleTypeOfTT(String tenantTypeCode, String roleCode){
        if(StringUtils.isBlank(tenantTypeCode)){
            throw new BusinessException(ErrorCode.E_130110);
        }
        if(StringUtils.isBlank(roleCode)){
            throw new BusinessException(ErrorCode.E_130125); 
        }
        tenantDao.unbindRoleOfTT(tenantTypeCode, roleCode);
	}
	
	@Override
	public TenantDTO createTenant(TenantDTO tenantDto) {
		if(tenantDto == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(tenantDto.getName()) ) {
			throw new BusinessException(ErrorCode.E_130111);
		}
		if(StringUtils.isBlank(tenantDto.getTenantTypeCode()) ) {
			throw new BusinessException(ErrorCode.E_130110);
		} 
        //去除首尾空格
		tenantDto.setName(tenantDto.getName().trim());
		tenantDto.setTenantTypeCode(tenantDto.getTenantTypeCode().trim());
		
//		List<Tenant> tenants = tenantDao.selectTenantByName(tenantDto.getName());
//		if(tenants != null && tenants.size() != 0){
//		    throw new BusinessException(ErrorCode.E_130127);
//		}
		
		//查询租户类型的角色信息（角色内包含权限编码）
		List<TenantTypeRole> roles = tenantDao.selectTenantTypeRoles(tenantDto.getTenantTypeCode());
		if(roles == null || roles.size() == 0){
		    throw new BusinessException(ErrorCode.E_130129);
		}
		//新增租户
		Tenant tenant = TenantDTO.convertDtoToEntity(tenantDto);
        tenantDao.insertTenant(tenant);
		//暂存租户的角色信息
		List<RoleDTO> roleList = new ArrayList<RoleDTO>();
		
		boolean flag = false;//标记租户类型内是否有权限，默认为false无权限
		//将租户类型信息角色信息复制，并改变角色编码为要求的格式
		for(TenantTypeRole role : roles) {
			RestResponse<RoleDTO> response = authService.queryRole(role.getRoleCode());
			if(response.getCode() != 0){
			    logger.info("[调用授权服务异常]{}", response.getMsg());
			    throw new BusinessException(ErrorCode.CUSTOM, response.getMsg());
			}
			//查询租户类型是否无权限
			RoleDTO roleDTO = response.getResult();
			//如果其中一个角色拥有权限，则改变标记变量
			if(roleDTO.getPrivileges() != null && roleDTO.getPrivileges().size() > 0){
			    flag = true;
			}
			roleDTO.setCode(String.format("%s_%s", roleDTO.getCode(), tenant.getId()));
			roleList.add(roleDTO);
		}
		//如果遍历完毕，租户类型的角色全部没有权限，则抛出异常
		if(!flag){
		    throw new BusinessException(ErrorCode.E_130130);
		}
		
		//为新建租户创建角色信息
		for(RoleDTO role : roleList) {
			TenantRoleDTO tenantRole = new TenantRoleDTO();
			tenantRole.setCode(role.getCode());
			RestResponse<RoleDTO> response = authService.createRole(role);
			if(response.getCode() != 0){
			    logger.info("[调用授权服务异常]{}", response.getMsg());
                throw new BusinessException(ErrorCode.CUSTOM, response.getMsg());
            }
			tenantDao.insertTenantRole(tenant.getId(), tenantRole);
		}
		
	    //查询并返回插入的租户信息（仅租户基础信息）
        Tenant tenantResult = tenantDao.selectTenantById(tenant.getId());
        return TenantDTO.convertEntityToDto(tenantResult);
	}

	@Override
	@Transactional
	public void removeTenant(Long tenantId) {
		if(tenantId == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
	    //删除租户
        Integer count = tenantDao.deleteTenant(tenantId);
        if(count == 0){
            throw new BusinessException(ErrorCode.E_130137);
        }
		//查询租户对应的角色，获取其中的code
		List<TenantRole> roles = tenantDao.selectTenantRoles(tenantId);
		if(roles != null && roles.size()>0) {
			List<String> roleCodeList = new ArrayList<String>();
			for(TenantRole role : roles) {
				roleCodeList.add(role.getRoleCode());
			}
			String[] roleCodes = new String[roleCodeList.size()];
			roleCodeList.toArray(roleCodes);
			authService.removeRole(roleCodes);
		}
		//删除租户内实体角色
		tenantDao.deleteTenantRoleByTenantId(tenantId);
		//删除租户和账号对应关系
		tenantDao.deleteTenantAccountByTenantId(tenantId);
	}

	@Override
	public void modifyTenant(TenantDTO tenant) {
		if(tenant == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(tenant.getId() == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		if(StringUtils.isBlank(tenant.getName())) {
			throw new BusinessException(ErrorCode.E_130111);
		} 
        //去除首尾空格
        tenant.setName(tenant.getName().trim());
		
		Integer count = tenantDao.updateTenant(tenant);
		if(count == 0){
            throw new BusinessException(ErrorCode.E_130137);
        }
	}

	@Override
	public TenantDTO queryTenant(Long id) {
		if(id == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		Tenant tenant = tenantDao.selectTenantById(id);
		if(tenant == null){
		    return new TenantDTO();
		} 
		return TenantDTO.convertEntityToDto(tenant);
	}

	@Override
	public List<TenantDTO> queryTenant(Long[] ids) {
		if(ArrayUtils.isEmpty(ids)) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		List<Tenant> list = tenantDao.selectTenantByIds(ids);
		return TenantDTO.convertEntitysToDtos(list);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<TenantDTO> pageTenantByConditions(TenantQueryParams queryParams, Integer pageNo, Integer pageSize) {
	    logger.info("[查询参数]pageNo:{}  pageSize:{}  {}", pageNo, pageSize, queryParams);
		PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
		List<Tenant> list = tenantDao.pageTenantByConditions(queryParams, pageRequest);
		Long total = tenantDao.countTenantByConditions(queryParams, pageRequest);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		
		//防止list为空时，entity转化dto失败
		List<TenantDTO> dtoList = new ArrayList<>(); 
		if(list != null && list.size() != 0){
		    dtoList = TenantDTO.convertEntitysToDtos(list);
		}
		
		Page<TenantDTO> page = new PageImpl<>(dtoList,pageable,total);
		logger.info("[查询结果]pageable:{}  total:{}  {}", pageable,total,dtoList);
		return page;
	}

	@Override
	public void createTenantRole(Long tenantId, TenantRoleDTO role) {
		if(tenantId == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		if(role == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(role.getCode()) ) {
			throw new BusinessException(ErrorCode.E_130112);
		}
		if(StringUtils.isBlank(role.getName()) ) {
            throw new BusinessException(ErrorCode.E_130111);
        } 
        //去除首尾空格
		role.setName(role.getName().trim());
		role.setCode(role.getCode().trim());
        
		// 调用授权服务接口（添加角色，给角色授权）
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setName(role.getName());
		roleDTO.setCode(role.getCode());
		List<PrivilegeDTO> privilegeList = new ArrayList<>();
		List<String> privilegeCodes = role.getPrivilegeCodes();
		for(String privilege : privilegeCodes) {
			PrivilegeDTO dto = new PrivilegeDTO();
			dto.setCode(privilege);
			privilegeList.add(dto);
		}
		roleDTO.setPrivileges(privilegeList);
		
		RestResponse<RoleDTO> newRole = authService.createRole(roleDTO);
		if(newRole.getCode() != 0){
		    logger.info("[调用授权服务异常]{}", newRole);
		    throw new BusinessException(ErrorCode.CUSTOM, newRole.getMsg());
		}
		tenantDao.insertTenantRole(tenantId, role);
	}

	@Override
	public void removeTenantRole(Long tenantId, String roleCode) {
		if(tenantId == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		if(StringUtils.isBlank(roleCode)) {
			throw new BusinessException(ErrorCode.E_130112);
		}
		// 调用授权服务接口（删除角色，删除角色权限）
		String[] roleCodes = {roleCode};
		
		Integer count = tenantDao.deleteTenantRole(tenantId, roleCode);
		if(count == 0){
		    throw new BusinessException(ErrorCode.E_130140);
		}
		
		/*
		 * 删除角色内实体，要考虑再授权服务中，该角色和owner是否有绑定关闭，如果有则不允许删除角色
		 */
		RestResponse<String> restResponse = authService.removeRole(roleCodes);
		logger.info("[调用授权服务]{}", restResponse);
		if(restResponse.getCode() == 0){
		    logger.info("[成功]");
		}else if(restResponse.getCode() == 120131){
		    throw new BusinessException(ErrorCode.E_130121);
		}else if(restResponse.getCode() == 120117){
		    throw new BusinessException(ErrorCode.E_130138);
		}else{
		    throw new BusinessException(ErrorCode.CUSTOM, restResponse.getMsg());
		}
	}
	
	@Override
	public void modifyTenantRole(Long tenantId, TenantRoleDTO role) {
		if(tenantId == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		if(role == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(role.getCode())) {
			throw new BusinessException(ErrorCode.E_130112);
		}
		if(StringUtils.isBlank(role.getName())) {
            throw new BusinessException(ErrorCode.E_130111);
        } 
        //去除首尾空格
        role.setName(role.getName().trim());
        role.setCode(role.getCode().trim());
        
        //校验租户id下是否有此角色(调用service方法，如果查询不到会抛出异常)
        queryTenantRole(tenantId, role.getCode());
		
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setName(role.getName());
		roleDTO.setCode(role.getCode());
		List<String> privilegeCodes = role.getPrivilegeCodes();
		if(privilegeCodes != null && privilegeCodes.size()>0) {
			// 调用授权服务接口（角色删除旧权限，添加新权限）
			List<PrivilegeDTO> privilegeList = new ArrayList<PrivilegeDTO>();
			for(String privilegeCode : privilegeCodes) {
				PrivilegeDTO privilege = new PrivilegeDTO();
				privilege.setCode(privilegeCode);
				privilegeList.add(privilege);
			}
			roleDTO.setPrivileges(privilegeList);
		}
		authService.modifyRole(roleDTO);
	}
	
	@Override
	public List<TenantRoleDTO> queryTenantRole(Long tenantId) {
		if(tenantId == null) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		List<TenantRole> list = tenantDao.selectTenantRoles(tenantId);
		//存储返回的结果
        List<TenantRoleDTO> roleDetailList = new ArrayList<TenantRoleDTO>();
        
		if(list != null && list.size()>0) {
			List<String> roleList = new ArrayList<String>();
			for(TenantRole role : list) {
				roleList.add(role.getRoleCode());
			}
			String[] roleCodes = new String[roleList.size()];
			roleCodes = roleList.toArray(roleCodes);
			//	 调用授权服务接口（查询角色名称列表）
			RestResponse<List<RoleDTO>> response = authService.queryRole(roleCodes);
			List<RoleDTO> roleDTOList = response.getResult();
			if(response.getCode() != 0){
			    throw new BusinessException(ErrorCode.CUSTOM, response.getMsg());
			}
			for(RoleDTO dto : roleDTOList){
			    TenantRoleDTO tenantRole = new TenantRoleDTO();
			    tenantRole.setCode(dto.getCode());
			    tenantRole.setName(dto.getName());
			    roleDetailList.add(tenantRole);
			}
		}
		return roleDetailList;
	}
	
	@Override
	public TenantRoleDTO queryTenantRole(Long tenantId, String roleCode) {
	    if(tenantId == null) {
            throw new BusinessException(ErrorCode.E_130108);
        }
        if(StringUtils.isBlank(roleCode)) {
            throw new BusinessException(ErrorCode.E_130112);
        }
		TenantRoleDTO dto = new TenantRoleDTO();
		// 调用授权服务接口（查询角色权限）
		RestResponse<RoleDTO> response = authService.queryRole(roleCode);
		logger.info("[调用授权服务]{}", response);
		if(response.getCode() == 120117){
		    throw new BusinessException(ErrorCode.E_130140);
		}else if(response.getCode()!=0) {
			throw new BusinessException(ErrorCode.CUSTOM, response.getMsg());
		}
		RoleDTO roleDTO = response.getResult();
		dto.setName(roleDTO.getName());
		dto.setCode(roleDTO.getCode());
		List<PrivilegeDTO> list = roleDTO.getPrivileges();
		List<String> privilegeCodes = new ArrayList<String>();
		for(PrivilegeDTO privilegeDTO : list) {
			privilegeCodes.add(privilegeDTO.getCode());
		}
		dto.setPrivilegeCodes(privilegeCodes);
		return dto;
	}

	@Transactional
	@Override
	public void createAccount(AccountDTO accountDTO) {
	    //校验账号
		if(StringUtils.isBlank(accountDTO.getUsername()) ) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(!VerifyUtil.verifyLength(accountDTO.getUsername(), 50)){
            throw new BusinessException(ErrorCode.E_130135);
        }
        if(!VerifyUtil.verifyCharacter(accountDTO.getUsername())){
            throw new BusinessException(ErrorCode.E_130134);
        }
        //校验密码
		if(StringUtils.isBlank(accountDTO.getPassword()) ) {
			throw new BusinessException(ErrorCode.E_130102);
		}
		//校验手机号
		if(StringUtils.isBlank(accountDTO.getMobile()) ) {
			throw new BusinessException(ErrorCode.E_130105);
		}
		if(!VerifyUtil.verifyPhoneNum(accountDTO.getMobile())){
            throw new BusinessException(ErrorCode.E_130136);
        }  
		
		//校验账号是否存在
		Account account2 = accountDao.selectAccountByUsernameIgnoreDel(accountDTO.getUsername());
		if(account2 != null && account2.getIsDel() == 0) {
			throw new BusinessException(ErrorCode.E_130115);
			
		}else if(account2 != null && account2.getIsDel() == 1){
		  //如果账号存在，但是isdel为1，则真实删除账号
          accountDao.deleteAccountIrrevocably(accountDTO.getUsername());
		}
		
		Account account = AccountDTO.convertToEntity(accountDTO);
		accountDao.insertAccount(account);
		accountDTO.setId(Integer.parseInt(Long.toString(account.getId())));
		List<TenantDTO> list = accountDTO.getTenants();
		if(list != null && list.size()>0) {
			accountDao.insertAccountTenant(account, list);
		}
	}

	
	@Override
	public void bind(String username, String tenantId) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(tenantId)) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		Account account = accountDao.selectAccountByUsername(username);
		AccountTenant at = accountDao.selectAccountTenantId(account.getId(), tenantId);
		if(at == null) {
			accountDao.insertAccountTenantId(account.getId(), tenantId);
		}
	}

	@Override
	public void removeAccount(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		accountDao.deleteAccount(username);
	}
		
	
	@Override
	public void unbind(String username, String tenantId) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(tenantId)) {
			throw new BusinessException(ErrorCode.E_130108);
		}
		Account account = accountDao.selectAccountByUsername(username);
		accountDao.deleteAccountTenantId(account.getId(), tenantId);
	}

	@Transactional
	@Override
	public void modifyAccountPassword(ModifyPasswordParams modifyPasswordParams) {
		if(modifyPasswordParams == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getUsername())) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getOldPassword())) {
			throw new BusinessException(ErrorCode.E_130113);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getNewPassword())) {
			throw new BusinessException(ErrorCode.E_130114);
		}
		Account account = accountDao.selectAccountByUsername(modifyPasswordParams.getUsername());
		if(account == null) {
			throw new BusinessException(ErrorCode.E_130103);
		}
		if(!modifyPasswordParams.getOldPassword().equals(account.getPassword())) {
			throw new BusinessException(ErrorCode.E_130116);
		}
		if(modifyPasswordParams.getOldPassword().equals(modifyPasswordParams.getNewPassword())) {
			throw new BusinessException(ErrorCode.E_130117);
		}
		accountDao.updateAccountPassword(modifyPasswordParams);
	}

	
	@Override
	public void modifyAccountPasswordNoOld(ModifyPasswordParams modifyPasswordParams) {
		if(modifyPasswordParams == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getUsername())) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getNewPassword())) {
			throw new BusinessException(ErrorCode.E_130114);
		}
		Account account = accountDao.selectAccountByUsername(modifyPasswordParams.getUsername());
		if(account == null) {
			throw new BusinessException(ErrorCode.E_130103);
		}
		accountDao.updateAccountPassword(modifyPasswordParams);
	}

	/**
	 * 找回密码
	 * @param modifyPasswordParams
	 */
	@Transactional
	@Override
	public void findAccountPassword(ModifyPasswordParams modifyPasswordParams) {
		if(modifyPasswordParams == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getUsername())) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getOldPassword())) {
			throw new BusinessException(ErrorCode.E_130113);
		}
		if(StringUtils.isBlank(modifyPasswordParams.getNewPassword())) {
			throw new BusinessException(ErrorCode.E_130114);
		}
		Account account = accountDao.selectAccountByUsername(modifyPasswordParams.getUsername());
		if(account == null) {
			throw new BusinessException(ErrorCode.E_130103);
		}
		//找回密码时 判断新老密码相等
		if(modifyPasswordParams.getOldPassword().equals(account.getPassword())) {
			throw new BusinessException(ErrorCode.E_130116);
		}
		/*if(modifyPasswordParams.getOldPassword().equals(modifyPasswordParams.getNewPassword())) {
			throw new BusinessException(ErrorCode.E_130117);
		}*/
		accountDao.updateAccountPassword(modifyPasswordParams);
	}
	
	@Transactional
	@Override
	public void modifyAccountMobile(ModifyMobileParams modifyMobileParams) {
		if(modifyMobileParams == null) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		if(StringUtils.isBlank(modifyMobileParams.getUsername())) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(!VerifyUtil.verifyLength(modifyMobileParams.getUsername(), 50)){
            throw new BusinessException(ErrorCode.E_130135);
        }
        if(!VerifyUtil.verifyCharacter(modifyMobileParams.getUsername())){
            throw new BusinessException(ErrorCode.E_130134);
        }
//		if(StringUtils.isBlank(modifyMobileParams.getCode())) {
//			throw new BusinessException(ErrorCode.E_100103);
//		}
		
        //校验手机号
        if(StringUtils.isBlank(modifyMobileParams.getMobile())) {
            throw new BusinessException(ErrorCode.E_130105);
        }
        if(!VerifyUtil.verifyPhoneNum(modifyMobileParams.getMobile())){
            throw new BusinessException(ErrorCode.E_130136);
        }  
        
		//TODO 调用接口获取到手机验证码
//		String code = "111111";
//		if(StringUtils.isBlank(code) || !code.equals(modifyMobileParams.getCode())) {
//			throw new BusinessException(ErrorCode.E_100102);
//		}
		accountDao.updateAccountMobile(modifyMobileParams);
	}

	@Override
	public AccountDTO queryAccount(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		if(!VerifyUtil.verifyLength(username, 50)){
            throw new BusinessException(ErrorCode.E_130135);
        }
        if(!VerifyUtil.verifyCharacter(username)){
            throw new BusinessException(ErrorCode.E_130134);
        }
		Account account = accountDao.selectAccountByUsername(username);
		if(account == null){
			throw new BusinessException(ErrorCode.E_130103);
		}
		AccountDTO dto = AccountDTO.convertEntityToDto(account);
		return dto;
	}

	@Override
	public List<AccountDTO> queryAccountByMobile(String mobile) {
		if(StringUtils.isBlank(mobile)) {
			throw new BusinessException(ErrorCode.E_130105);
		}
		if(!VerifyUtil.verifyPhoneNum(mobile)){
            throw new BusinessException(ErrorCode.E_130136);
        }
		List<Account> accounts = accountDao.selectAccountByMobile(mobile);
		List<AccountDTO> dtos = AccountDTO.convertEntityToDto(accounts);
		return dtos;
	}

	@Override
	public List<AccountDTO> queryAccount(String[] usernames) {
		if(ArrayUtils.isEmpty(usernames)) {
			throw new BusinessException(ErrorCode.E_100101);
		}
		for(int i = 0; i< usernames.length; i++){
		    if(!VerifyUtil.verifyLength(usernames[i], 50)){
	            throw new BusinessException(ErrorCode.E_130135);
	        }
	        if(!VerifyUtil.verifyCharacter(usernames[i])){
	            throw new BusinessException(ErrorCode.E_130134);
	        }
		}
		
		List<Account> list = accountDao.queryAccount(usernames);
		List<AccountDTO> dtoList = AccountDTO.convertEntityToDto(list);
		return dtoList;
	}

	@Override
	public Page<AccountDTO> pageAccountByConditions(AccountQueryParams queryParams, Integer pageNo, Integer pageSize) {
		PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
		List<Account> list = accountDao.pageAccountByConditions(queryParams, pageRequest);
		Long total = accountDao.countAccountByConditions(queryParams);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<AccountDTO> dtoList = new ArrayList<>(); 
		if(list.size() != 0){
		    dtoList = AccountDTO.convertEntityToDto(list);
		}
		
		Page<AccountDTO> page = new PageImpl<>(dtoList,pageable,total);
		return page;
	}

	@Override
	public boolean isExistedUsername(String username) {
		if(StringUtils.isBlank(username)) {
			throw new BusinessException(ErrorCode.E_130101);
		}
		Account account = accountDao.selectAccountByUsername(username);
		if(account == null) {
			return false;
		}else {
			return true;
		}
	}

}
