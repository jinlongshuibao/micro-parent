package com.uiotsoft.micro.api.synchronization.dto;

import org.springframework.beans.BeanUtils;

import lombok.Data;

/**  
 * 用户和sn关系
 * @author 杨小波
 * @date 2018年10月24日 15:20:40  
 */
@Data
public class User2SNDataDTO {
    private Long id;
    private String sn;
    private String username;
    private String userPwd;
    private Integer userId;
    private Integer regSnId;
    private String si;
    private Integer isComplete;//是否成功发送    0未成功，1成功
    private Integer retryType;//重试类型。1为新增，2为修改密码
    
}
