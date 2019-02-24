package com.uiotsoft.micro.authorization.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PrivilegeEntity", description = "权限信息")
@Data
public class PrivilegeEntity  {
    
    @ApiModelProperty("权限id")
    private Long id;
    
    @ApiModelProperty("所属权限组id")
    private Long privilegeGroupId;

    @ApiModelProperty("权限编码")
    private String code;

    @ApiModelProperty("权限名称")
    private String name;
   
    @ApiModelProperty("状态")
    private Integer status;

}
