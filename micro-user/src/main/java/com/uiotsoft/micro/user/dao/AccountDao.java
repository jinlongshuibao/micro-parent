package com.uiotsoft.micro.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.user.dto.AccountQueryParams;
import com.uiotsoft.micro.user.dto.ModifyMobileParams;
import com.uiotsoft.micro.user.dto.ModifyPasswordParams;
import com.uiotsoft.micro.user.dto.TenantDTO;
import com.uiotsoft.micro.user.entity.Account;
import com.uiotsoft.micro.user.entity.AccountTenant;
import com.uiotsoft.micro.user.entity.Tenant;

public interface AccountDao {
    /**
     * 根据用户名查询账号信息(不判断isdel字段)
     * @param username
     */
    Account selectAccountByUsernameIgnoreDel(@Param("username")String username);
	/**
	 * 根据用户名查询账号信息
	 * @param username
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月24日 下午4:40:21
	 */
	Account selectAccountByUsername(@Param("username")String username);
	/**
	 * 跟进手机号查询用户
	 * @param mobile
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 上午10:08:04
	 */
	List<Account> selectAccountByMobile(@Param("mobile")String mobile);
	/**
	 * 创建账号
	 * @param account
	 * @author 孔得峰
	 * @date 2018年7月25日 上午11:25:36
	 */
	void insertAccount(Account account);
	/**
	 * 添加账号租户关系
	 * @param account
	 * @author 孔得峰
	 * @param list 
	 * @date 2018年7月27日 下午7:02:12
	 */
	void insertAccountTenant(@Param("account")Account account, @Param("list")List<TenantDTO> list);
	/**
	 * 根据用户名列表查询账号列表
	 * @param usernames
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月25日 下午2:17:58
	 */
	List<Account> queryAccount(@Param("usernames")String[] usernames);
	/**
	 * 删除账号
	 * @param username
	 * @author 孔得峰
	 * @date 2018年7月25日 下午2:34:33
	 */
	void deleteAccount(@Param("username")String username);
	
	/**
     * 真实删除账号
     * @param username
     */
    void deleteAccountIrrevocably(@Param("username")String username);
	
	/**
	 * 修改密码
	 * @param modifyPasswordParams
	 * @author 孔得峰
	 * @date 2018年7月26日 上午10:15:36
	 */
	void updateAccountPassword(@Param("params")ModifyPasswordParams modifyPasswordParams);
	/**
	 * 修改手机号
	 * @param modifyMobileParams
	 * @author 孔得峰
	 * @date 2018年7月26日 上午10:33:26
	 */
	void updateAccountMobile(@Param("params")ModifyMobileParams modifyMobileParams);
	/**
	 * 根据条件分页查询账号
	 * @param queryParams
	 * @param pageRequest
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月26日 上午10:58:55
	 */
	List<Account> pageAccountByConditions(@Param("params")AccountQueryParams queryParams, @Param("page")PageRequestParams pageRequest);
	/**
	 * 根据条件查询账号总数
	 * @param queryParams
	 * @param pageRequest
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月26日 上午11:02:45
	 */
	Long countAccountByConditions(@Param("params")AccountQueryParams queryParams);
	/**
	 * 账号绑定租户
	 * @param accountId
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年8月16日 上午11:43:44
	 */
	void insertAccountTenantId(@Param("accountId")Long accountId, @Param("tenantId")String tenantId);
	/**
	 * 账号解绑租户
	 * @param id
	 * @param tenantId
	 * @author 孔得峰
	 * @date 2018年8月16日 下午2:08:36
	 */
	void deleteAccountTenantId(@Param("accountId")Long accountId, @Param("tenantId")String tenantId);
	/**
	 * 获取账号和租户关系
	 * @param id
	 * @param tenantId
	 * @return
	 * @author 孔得峰
	 * @date 2018年8月16日 下午3:43:05
	 */
	AccountTenant selectAccountTenantId(@Param("accountId")Long accountId, @Param("tenantId")String tenantId);
	/**
	 * 根据账号ID查询租户列表
	 * @param id
	 * @return
	 * @author 孔得峰
	 * @date 2018年8月16日 下午4:57:32
	 */
	List<Tenant> selectTenantsByAccountId(@Param("accountId")Long accountId);
	
}
