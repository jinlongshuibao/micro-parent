package com.uiotsoft.micro.api.user.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AccountDTO", description = "账号信息")
public class AccountDTO {
	
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
    
}
