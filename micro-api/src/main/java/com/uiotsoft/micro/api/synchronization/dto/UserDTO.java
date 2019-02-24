package com.uiotsoft.micro.api.synchronization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserDTO", description = "用户信息DTO")
public class UserDTO {

    @ApiModelProperty("用户id")
    private String userId;
    
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("真实姓名")
    private String truename;

}
