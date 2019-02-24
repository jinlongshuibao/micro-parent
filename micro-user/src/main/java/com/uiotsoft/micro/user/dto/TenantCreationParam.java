package com.uiotsoft.micro.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TenantCreationParam", description = "租户和账号信息")
public class TenantCreationParam {
    
    @ApiModelProperty("租户ID")
    private Long id;
    
    @ApiModelProperty("租户名称")
    private String name;
    
    @ApiModelProperty("租户类型编码")
    private String tenantTypeCode;
    
    @ApiModelProperty("手机号")
    private String phoneNum;
    
    @ApiModelProperty("账号")
    private String username;
    
    @ApiModelProperty("密码")
    private String password;
    
    @ApiModelProperty("账号是否存在")
    private boolean accountExist;
    
}
