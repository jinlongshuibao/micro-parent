package com.uiotsoft.micro.api.synchronization.dto;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserInfoDTO", description = "用户信息")
public class UserInfoDTO {

    @ApiModelProperty("主机SN")
    private String SN;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机si")
    private String si;

    @ApiModelProperty("SN的id")
    private Integer regSnId;

    @ApiModelProperty("用户角色")
    private Integer userRole;

    @ApiModelProperty("密码")
    private String userPwd;

    @ApiModelProperty("")
    private String userDoorPwd;

    @ApiModelProperty("")
    private String userLandState;

    @ApiModelProperty("")
    private String userSex;

    @ApiModelProperty("")
    private String userBirth;

    @ApiModelProperty("")
    private String isMarket;

    @ApiModelProperty("")
    private Integer userTag;

    @ApiModelProperty("")
    private Integer roleId;

    @ApiModelProperty("")
    private Date createDate;

    @ApiModelProperty("")
    private String userType;

    @ApiModelProperty("")
    private String oemFirm;

    @ApiModelProperty("")
    private String headPortrait;

    @ApiModelProperty("")
    private String bindUser;

    @ApiModelProperty("")
    private String areaCode;

}
