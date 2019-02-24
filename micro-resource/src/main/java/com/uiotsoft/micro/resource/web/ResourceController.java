package com.uiotsoft.micro.resource.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uiotsoft.micro.common.domain.RestResponse;
import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.dto.ResourceDTO;
import com.uiotsoft.micro.resource.service.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/resource")
@Api(value = "资源服务", tags = "ResourceApi", description="包含应用、资源等信息维护")
public class ResourceController {
	
	@Autowired
	private ResourceService resourceService;
	
	@ApiOperation("创建应用")
	@ApiImplicitParam(name = "application", value = "应用", required = true, dataType = "ApplicationDTO", paramType="body")
	@PostMapping(value = "/application")
	public RestResponse<Nullable> createApplication(@RequestBody ApplicationDTO application){
		resourceService.createApplication(application);
		return RestResponse.success();
	}
	
	@ApiOperation("删除应用")
	@ApiImplicitParam(name = "applicationCode", value = "应用编码", required = true, dataType = "String", paramType="path")
	@DeleteMapping(value = "/application/{applicationCode}")
	public RestResponse<Nullable> removeApplication(@PathVariable("applicationCode") String applicationCode){
		resourceService.removeApplication(applicationCode);
		return RestResponse.success();
	}
	
	@ApiOperation("修改应用")
	@ApiImplicitParam(name = "application", value = "应用", required = true, dataType = "ApplicationDTO", paramType="body")
	@PutMapping(value = "/application")
	public RestResponse<Nullable> modifyApplication(@RequestBody ApplicationDTO application){
		resourceService.modifyApplication(application);
		return RestResponse.success();
	}
	
	@ApiOperation("查询单个应用")
	@ApiImplicitParam(name = "applicationCode", value = "应用编码", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/application/{applicationCode}")
	public RestResponse<ApplicationDTO> queryApplication(@PathVariable("applicationCode") String applicationCode){
		ApplicationDTO dto = resourceService.queryApplication(applicationCode);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("分页条件检索应用")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "应用查询参数", required = false, dataType = "ApplicationQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType="query")
	})
	@PostMapping(value = "/application/page")
	public RestResponse<Page<ApplicationDTO>> pageApplicationByConditions(@RequestBody ApplicationQueryParams queryParams,@RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<ApplicationDTO> page = resourceService.pageApplicationByConditions(queryParams, pageNo, pageSize);
		return RestResponse.success(page);
	}
	
	@ApiOperation("加载指定应用的资源")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "privileageCodes", value = "权限列表", required = true, dataTypeClass = List.class, paramType="body"),
		@ApiImplicitParam(name = "applicationCode", value = "应用编码", required = true,  dataType = "String", paramType="path")
	})
	@PostMapping(value = "/{applicationCode}")
	public RestResponse<ResourceDTO> loadResources(@RequestBody List<String> privileageCodes, @PathVariable("applicationCode") String applicationCode){
		ResourceDTO dto = resourceService.loadResources(privileageCodes, applicationCode);
		return RestResponse.success(dto);
	}
	

}
