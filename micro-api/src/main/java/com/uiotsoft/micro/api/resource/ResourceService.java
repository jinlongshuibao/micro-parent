package com.uiotsoft.micro.api.resource;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.uiotsoft.micro.api.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.api.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.api.resource.dto.MenuDTO;
import com.uiotsoft.micro.api.resource.dto.MenuQueryParams;
import com.uiotsoft.micro.api.resource.dto.ResourceDTO;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.RestResponse;

@FeignClient(value = "micro-resource",fallback=ResourceRemoteHystrix.class)
public interface ResourceService {
	
	@PostMapping(value = "/resource/application")
	public RestResponse<Nullable> createApplication(@RequestBody ApplicationDTO application);
	
	@DeleteMapping(value = "/resource/application/{applicationCode}")
	public RestResponse<Nullable> removeApplication(@PathVariable("applicationCode") String applicationCode);
	
	@PutMapping(value = "/resource/application")
	public RestResponse<Nullable> modifyApplication(@RequestBody ApplicationDTO application);
	
	@GetMapping(value = "/resource/resource/application/{applicationCode}")
	public RestResponse<ApplicationDTO> queryApplication(@PathVariable("applicationCode") String applicationCode);

	@PostMapping(value = "/resource/application/page")
	public RestResponse<Page<ApplicationDTO>> pageApplicationByConditions(@RequestBody ApplicationQueryParams params
			,@RequestParam Integer pageNo, @RequestParam Integer pageSize);
	
	@PostMapping(value = "/resource/{applicationCode}")
	public RestResponse<ResourceDTO> loadResources(@RequestBody List<String> privileageCodes, @PathVariable("applicationCode") String applicationCode);
	
	@PostMapping(value = "/resource/menu/")
    public RestResponse<Nullable> createMenu(@RequestBody MenuDTO menu);
	
	@DeleteMapping(value = "/resource/menu/{menuId}")
    public RestResponse<Nullable> removeMenu(@PathVariable("menuId")Long menuId);
    
    @PutMapping(value = "/resource/menu/")
    public RestResponse<Nullable> modifyMenu(@RequestBody MenuDTO menu);
    
    @GetMapping(value = "/resource/menu/{menuId}")
    public RestResponse<MenuDTO> queryMenu(@PathVariable("menuId")Long menuId);
    
    @GetMapping(value = "/resource/menu/applicationCode/{applicationCode}")
    public RestResponse<List<MenuDTO>> queryMenuByApplicationCode(@PathVariable("applicationCode")String applicationCode);
    
    @PostMapping(value = "/resource/menu/condition")
    public RestResponse<List<MenuDTO>> queryMenu(@RequestBody MenuQueryParams params);
}

@Component
class ResourceRemoteHystrix implements ResourceService{
	
	@Override
	public RestResponse<ApplicationDTO> queryApplication(String applicationCode) {
		return new RestResponse<ApplicationDTO>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

	@Override
	public RestResponse<Nullable> createApplication(ApplicationDTO application) {
		return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

	@Override
	public RestResponse<Nullable> removeApplication(String applicationCode) {
		return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

	@Override
	public RestResponse<Nullable> modifyApplication(ApplicationDTO application) {
		return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

	@Override
	public RestResponse<Page<ApplicationDTO>> pageApplicationByConditions(ApplicationQueryParams params, Integer pageNo,
			Integer pageSize) {
		return new RestResponse<Page<ApplicationDTO>>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

	@Override
	public RestResponse<ResourceDTO> loadResources(List<String> privileageCodes, String applicationCode) {
		return new RestResponse<ResourceDTO>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
	}

    @Override
    public RestResponse<Nullable> createMenu(MenuDTO menu) {
        return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

    @Override
    public RestResponse<Nullable> removeMenu(Long menuId) {
        return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

    @Override
    public RestResponse<Nullable> modifyMenu(MenuDTO menu) {
        return new RestResponse<Nullable>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

    @Override
    public RestResponse<MenuDTO> queryMenu(Long menuId) {
        return new RestResponse<MenuDTO>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

    @Override
    public RestResponse<List<MenuDTO>> queryMenuByApplicationCode(String applicationCode) {
        return new RestResponse<List<MenuDTO>>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

    @Override
    public RestResponse<List<MenuDTO>> queryMenu(MenuQueryParams params) {
        return new RestResponse<List<MenuDTO>>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

}
