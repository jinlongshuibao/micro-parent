package com.uiotsoft.micro.api.synchronization.dto;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import lombok.Data;

/**  
 * @author 杨小波
 * @date 2018年11月15日 16:25:29  
 */
@Data
public class SnRegisterDTO {
    private Integer id;

    private String sn;

    private String snName;

    private String username;

    private String hardDiskNum;

    private String mac;

    private String busiId;

    private String snType;

    private Integer snScanPhase;

    private String agentSidFk;

    private Date takeEndDate;

    private Integer takeCount;

    private String inviteCode;

    private Integer receState;

    private String isDel;

    private Integer smsCount;

    private Integer voiceCount;

    private String snLang;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}
