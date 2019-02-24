package com.uiotsoft.micro.api.resource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MenuQueryParams", description = "菜单查询参数")
public class MenuQueryParams {

	@ApiModelProperty("所属应用")
    private String applicationCode;
	
	@ApiModelProperty("菜单标题")
    private String title;
	
	@ApiModelProperty("状态")
    private Integer status;
	

}
