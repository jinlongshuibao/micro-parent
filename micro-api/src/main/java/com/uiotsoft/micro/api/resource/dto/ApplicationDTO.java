package com.uiotsoft.micro.api.resource.dto;

import io.swagger.annotations.ApiModel;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "ApplicationDTO", description = "应用信息")
public class ApplicationDTO {


	@ApiModelProperty("应用id")
	private Long id;

	@ApiModelProperty("应用名称")
    private String name;

	@ApiModelProperty("应用编码")
    private String code;
    

     
}
