package com.uiotsoft.micro.user.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.uiotsoft.micro.user.entity.Account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AccountDTO", description = "账号信息")
public class AccountDTO {
	
	@ApiModelProperty("账号ID")
	private int id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 区域编号
     */
    @ApiModelProperty("区域编号")
    private String areaCode;
    /**
	 * 手机号
	 */
    @ApiModelProperty("手机号")
	private String mobile;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 所属租户
     */
    @ApiModelProperty("所属租户")
    private List<TenantDTO> tenants = new ArrayList<>();
    
    /**
     * 将entity转为DTO
     * @param entity
     * @return
     * @author 孔得峰
     * @date 2018年7月24日 下午5:20:02
     */
    public static AccountDTO convertEntityToDto(Account entity){
		AccountDTO dto = new AccountDTO();
		BeanUtils.copyProperties(entity, dto);
		dto.setId(entity.getId().intValue());
		return dto;
	}
    /**
     * 将entity列表转为dto列表
     * @param list
     * @return
     * @author 孔得峰
     * @date 2018年7月25日 下午2:17:09
     */
    public static List<AccountDTO> convertEntityToDto(List<Account> list) {
    	List<AccountDTO> dtoList = new ArrayList<>();
		if(list != null && list.size()>0) {
			for(Account account : list) {
				dtoList.add(convertEntityToDto(account));
			}
		}
		return dtoList;
	}
    
    public static Account convertToEntity(AccountDTO dto){
    	Account account = new Account();
		BeanUtils.copyProperties(dto, account);
		account.setId(new Long(dto.getId()));
		return account;
	}
}
