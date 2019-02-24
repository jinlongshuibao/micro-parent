package com.uiotsoft.micro.api.synchronization;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uiotsoft.micro.api.synchronization.dto.UserInfoDTO;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.RestResponse;
/**  
 * @author 杨小波
 */
@FeignClient(value = "micro-synchronization",fallback=SynchronizationRemoteHystrix.class)
public interface SynchronizationService {
    
    @PostMapping(value = "/smartHomeSync/addUser")
    public RestResponse<Nullable> addUser(@RequestBody UserInfoDTO userInfo);
}



/*
 * 熔断处理
 */
@Component
class SynchronizationRemoteHystrix implements SynchronizationService{

    @Override
    public RestResponse<Nullable> addUser(UserInfoDTO userInfo) {
        return new RestResponse<Nullable>(ErrorCode.E_999994.getCode(), ErrorCode.E_999994.getDesc());
    }

}
