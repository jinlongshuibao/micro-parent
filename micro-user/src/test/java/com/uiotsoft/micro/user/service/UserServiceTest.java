package com.uiotsoft.micro.user.service;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.common.domain.RestResponse;
import com.uiotsoft.micro.user.UserServer;
import com.uiotsoft.micro.user.dto.AccountDTO;
import com.uiotsoft.micro.user.dto.AccountQueryParams;
import com.uiotsoft.micro.user.dto.AuthenticationInfo;
import com.uiotsoft.micro.user.dto.FastAuthenticationInfo;
import com.uiotsoft.micro.user.dto.ModifyMobileParams;
import com.uiotsoft.micro.user.dto.ModifyPasswordParams;
import com.uiotsoft.micro.user.dto.TenantDTO;
import com.uiotsoft.micro.user.dto.TenantQueryParams;
import com.uiotsoft.micro.user.dto.TenantRoleDTO;
import com.uiotsoft.micro.user.dto.TenantTypeDTO;
import com.uiotsoft.micro.user.dto.TenantTypeRoleDTO;


@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {UserServer.class})
public class UserServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
	
	@Autowired
	private UserService userService;
	
	String username = "admin";
	String passowrd = "666666";
	String mobile = "13938531013";
	
	/**
	 * 测试创建租户类型
	 * 
	 * @author 孔得峰
	 * @date 2018年7月26日 下午2:30:26
	 */
	@Test
	public void testCreateTenantType(){
		TenantTypeDTO tenantType = new TenantTypeDTO();
		tenantType.setName("紫光物联内部员工");
		tenantType.setCode("ZGWLNBYG");
		List<TenantTypeRoleDTO> roles = new ArrayList<TenantTypeRoleDTO>();
		for(int i=1; i<=10; i++) {
			TenantTypeRoleDTO role = new TenantTypeRoleDTO();
			String code = "R"+i;
			String name = "角色"+i;
			role.setRoleCode(code);
			role.setRoleName(name);
			roles.add(role);
		}
		tenantType.setRoles(roles);
		userService.createTenantType(tenantType);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试修改租户类型
	 * 
	 * @author 孔得峰
	 * @date 2018年7月26日 下午7:11:05
	 */
	@Test
	public void testModifyTenantType(){
		TenantTypeDTO tenantType = new TenantTypeDTO();
		tenantType.setName("紫光物联内部员工");
		tenantType.setCode("ZGWLNBYG");
		List<TenantTypeRoleDTO> roles = new ArrayList<TenantTypeRoleDTO>();
		for(int i=1; i<=10; i++) {
			TenantTypeRoleDTO role = new TenantTypeRoleDTO();
			String code = "RR"+i;
			String name = "角色"+i;
			role.setRoleCode(code);
			role.setRoleName(name);
			roles.add(role);
		}
		tenantType.setRoles(roles);
		userService.modifyTenantType(tenantType);
		System.out.println("测试结束");
	}
	
	/**
	 * 获取所有租户类型
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 上午9:24:33
	 */
	@Test
	public void testQueryAllTenantType(){
		List<TenantTypeDTO> dtoList = userService.queryAllTenantType();
		for(TenantTypeDTO dto : dtoList) {
			System.out.println(dto.getName()+dto.getCode());
		}
		System.out.println("测试结束");
	}
	
	/**
	 * 查询某租户类型
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 上午9:32:38
	 */
	@Test
	public void testQueryTenantType(){
		String code = "ZGWLNBYG";
		TenantTypeDTO dto = userService.queryTenantType(code);
		System.out.println(dto.getName()+"-"+dto.getCode());
		List<TenantTypeRoleDTO> roles = dto.getRoles();
		if(roles != null && roles.size()>0) {
			for(TenantTypeRoleDTO role : roles) {
				System.out.println(role.getRoleName()+"-"+role.getRoleCode());
			}
		}
		System.out.println("测试结束");
	}
	
	/**
	 * 测试删除租户类型
	 * 
	 * @author 孔得峰
	 * @date 2018年7月26日 下午3:07:18
	 */
	@Test
	public void testRemoveTenantType(){
		userService.removeTenantType("ceshi2");
		System.out.println("测试结束");
	}
	
	/**
	 * 测试创建租户
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:12:38
	 */
	@Test
	public void testCreateTenant(){
		TenantDTO tenant = new TenantDTO();
		tenant.setName("管理员");
		tenant.setTenantTypeCode("admin");
		userService.createTenant(tenant);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试修改租户
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:13:05
	 */
	@Test
	public void testModityTenant(){
		TenantDTO tenant = new TenantDTO();
		tenant.setId(1L);
		tenant.setName("管理员");
		userService.modifyTenant(tenant);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试查询单个租户
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:22:55
	 */
	@Test
	public void testQueryTenant(){
		Long tenantId = 1L;
		TenantDTO dto = userService.queryTenant(tenantId);
		System.out.println(dto.getName()+"-"+dto.getTenantTypeCode());
		System.out.println("测试结束");
	}
	/**
	 * 测试查询多个租户
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:33:13
	 */
	@Test
	public void testQueryTenants(){
		Long[] ids = {1L,2L,3L};
		List<TenantDTO> dtoList = userService.queryTenant(ids);
		for(TenantDTO dto : dtoList) {
			System.out.println(dto.getName()+"-"+dto.getTenantTypeCode());
		}
		System.out.println("测试结束");
	}
	/**
	 * 测试多条件分页查询租户
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:33:22
	 */
	@Test
	public void testPageTenantByConditions(){
		TenantQueryParams query = new TenantQueryParams();
		query.setName("公");
		Page<TenantDTO> page = userService.pageTenantByConditions(query , 2, 2);
		for(TenantDTO dto : page){
			logger.info("Tenant name : {}", dto.getName());
		}
		System.out.println("测试结束");
	}
	/**
	 * 测试创建租户内角色
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午5:45:13
	 */
	@Test
	public void testCreateTenantRole(){
		Long tenantId = 1L;
		TenantRoleDTO dto = new TenantRoleDTO();
		dto.setName("角色1");
		dto.setCode("R1");
		userService.createTenantRole(tenantId, dto);
		System.out.println("测试结束");
	}
	/**
	 * 测试修改租户内角色
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午6:37:58
	 */
	@Test
	public void testModifyTenantRole(){
		// TODO 暂时不能测试
		Long tenantId = 1L;
		TenantRoleDTO dto = new TenantRoleDTO();
		dto.setName("角色1");
		dto.setCode("R1");
		userService.modifyTenantRole(tenantId, dto);
		System.out.println("测试结束");
	}
	/**
	 * 测试查询租户内角色
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午6:38:11
	 */
	@Test
	public void testQueryTenantRoles(){
		// TODO 暂时不能测试
		Long tenantId = 1L;
		List<TenantRoleDTO> dtoList = userService.queryTenantRole(tenantId);
		System.out.println(dtoList);
		System.out.println("测试结束");
	}
	/**
	 * 测试查询租户内角色信息
	 * 
	 * @author 孔得峰
	 * @date 2018年7月27日 下午6:38:11
	 */
	@Test
	public void testQueryTenantRole(){
		// TODO 暂时不能测试
		Long tenantId = 1L;
		String roleCode = "R1";
		TenantRoleDTO dto = userService.queryTenantRole(tenantId, roleCode);
		System.out.println(dto);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试账号是否存在
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:38:34
	 */
	@Test
	public void testAccountIsExisted(){
		String username = "admin2";
		boolean flag = userService.isExistedUsername(username);
		System.out.println(flag);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试创建账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:38:43
	 */
	@Test
	public void testCreateAccount(){
		AccountDTO dto = new AccountDTO();
		dto.setUsername("admin2");
		dto.setMobile("13938531014");
		dto.setPassword("123456");
		userService.createAccount(dto);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试修改账号密码
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:38:54
	 */
	@Test
	public void testModifyAccountPassword(){
		ModifyPasswordParams params = new ModifyPasswordParams();
		params.setUsername("admin2");
		params.setOldPassword("6543210");
		params.setNewPassword("123456");
		userService.modifyAccountPassword(params);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试修改账号手机号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:39:04
	 */
	@Test
	public void testModifyAccountMobile(){
		ModifyMobileParams params = new ModifyMobileParams();
		params.setUsername("admin2");
		params.setMobile("13938531012");
		params.setCode("111112");
		userService.modifyAccountMobile(params);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试查询账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:39:17
	 */
	@Test
	public void testQueryAccount(){
		String username = "admin2";
		AccountDTO dto = userService.queryAccount(username);
		System.out.println(dto.toString());
		System.out.println("测试结束");
	}
	
	/**
	 * 测试根据手机号查询账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:39:27
	 */
	@Test
	public void testQueryAccountByMobile(){
		String mobile = "13938531012";
		List<AccountDTO> dto = userService.queryAccountByMobile(mobile);
		System.out.println(dto);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试根据用户名（多个）查询账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:39:39
	 */
	@Test
	public void testQueryAccountByUsernames(){
		String[] usernames = {"admin", "admin2"};
		List<AccountDTO> dtoList = userService.queryAccount(usernames);
		System.out.println(dtoList.toString());
		System.out.println("测试结束");
	}
	
	/**
	 * 根据条件查询账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:39:58
	 */
	@Test
	public void testQueryAccountByConditions(){
		AccountQueryParams params = new AccountQueryParams();
		params.setUsername("ad");
		Integer pageNo = 1;
		Integer pageSize = 1;
		Page<AccountDTO> page = userService.pageAccountByConditions(params, pageNo, pageSize);
		System.out.println(page.toString());
		System.out.println("测试结束");
	}
	
	/**
	 * 测试删除账号
	 * 
	 * @author 孔得峰
	 * @date 2018年7月28日 上午9:40:10
	 */
	@Test
	public void testRemoveAccount(){
		String username = "admin2";
		userService.removeAccount(username);
		System.out.println("测试结束");
	}
	
	/**
	 * 测试用户认证
	 * @author 孔得峰
	 * @date 2018年7月25日 上午10:11:16
	 */
	@Test
	public void testAuthentication(){
		AuthenticationInfo authenticationInfo = new AuthenticationInfo();
		authenticationInfo.setUsername(username);
		authenticationInfo.setPassword(passowrd);
		AccountDTO dto = userService.authentication(authenticationInfo);
		Assert.assertEquals(0, RestResponse.success(dto).getCode());
	}
	
	/**
	 * 测试用户快速认证 
	 * @author 孔得峰
	 * @date 2018年7月25日 上午10:11:31
	 */
	@Test
	public void testFastAuthentication(){
		FastAuthenticationInfo authenticationInfo = new FastAuthenticationInfo();
		authenticationInfo.setMobile(mobile);
		AccountDTO dto = userService.fastAuthentication(authenticationInfo);
		Assert.assertEquals(0, RestResponse.success(dto).getCode());
	}
	

}
