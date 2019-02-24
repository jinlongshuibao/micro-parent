package com.uiotsoft.micro.api.synchronization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SNInfo", description = "SN信息")
public class SNInfoDTO {
    
    @ApiModelProperty("sn注册表id")
    private Integer regSnId;
    
    @ApiModelProperty("代理商标志")
    private String angentSidFk;
    
    @ApiModelProperty("主机（SN）、路由器（SNR）、酒店(SNH)、酒店路由(SNHR)")
    private String snType;

    @ApiModelProperty("主机子类型仅平台用")
    private String snTypeSub;
    
}
