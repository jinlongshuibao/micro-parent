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

import com.uiotsoft.micro.api.resource.dto.MenuDTO;
import com.uiotsoft.micro.api.resource.dto.MenuQueryParams;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.RestResponse;

@FeignClient(value = "micro-resource",fallback=ApplicationRemoteHystrix.class)
public interface MenuService {
	
	@PostMapping(value = "/resource/menu")
	public RestResponse<Nullable> createMenu(@RequestBody MenuDTO menu);
	
	@DeleteMapping(value = "/resource/menu/{menuId}")
	public RestResponse<Nullable> removeMenu(@PathVariable("menuId")Long menuId);
	
	@PutMapping(value = "/resource/menu")
	public RestResponse<Nullable> modifyMenu(@RequestBody MenuDTO menu);
	
	@GetMapping(value = "/resource/menu/{menuId}")
	public RestResponse<MenuDTO> queryMenu(@PathVariable("menuId")Long menuId);
	
	@GetMapping(value = "/resource/menu/applicationCode/{applicationCode}")
	public RestResponse<List<MenuDTO>> queryMenuByApplicationCode(@PathVariable("applicationCode")String applicationCode);
	
	@PostMapping(value = "/resource/menu/condition")
	public RestResponse<Page<MenuDTO>> queryMenu(@RequestBody MenuQueryParams params, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
}

@Component
class ApplicationRemoteHystrix implements MenuService{
	
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
    public RestResponse<Page<MenuDTO>> queryMenu(MenuQueryParams params, Integer pageNo, Integer pageSize) {
        return new RestResponse<Page<MenuDTO>>(ErrorCode.E_999993.getCode(), ErrorCode.E_999993.getDesc());
    }

}
