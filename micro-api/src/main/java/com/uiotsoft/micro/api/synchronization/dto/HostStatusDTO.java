package com.uiotsoft.micro.api.synchronization.dto;

import lombok.Data;

/**  
 * 主机发送的MQ的JSON信息
 * @author 杨小波
 * @date 2018年10月25日 10:27:34  
 */
@Data
public class HostStatusDTO {
    private String messageType;//消息类型
    private String sn;//主机sn
    private String status;//主机状态（上线/下线）
    
}
