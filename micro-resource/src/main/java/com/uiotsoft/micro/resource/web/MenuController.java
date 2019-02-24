package com.uiotsoft.micro.resource.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.dto.MenuQueryParams;
import com.uiotsoft.micro.resource.service.MenuService;

@RestController
@RequestMapping(value = "/resource/menu")
@Api(value = "菜单服务", tags = "MenuApi", description="菜单信息维护")
public class MenuController {
	@Autowired
	private MenuService menuService;
	
	@ApiOperation("创建菜单")
	@ApiImplicitParam(name = "menu", value = "菜单", required = true, dataType = "MenuDTO", paramType="body")
	@PostMapping(value = "/")
	public RestResponse<Nullable> createMenu(@RequestBody MenuDTO menu){
		menuService.createMenu(menu);
		return RestResponse.success();
	}
	
	@ApiOperation("删除菜单")
	@ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "Long", paramType="path")
	@DeleteMapping(value = "/{menuId}")
	public RestResponse<Nullable> removeMenu(@PathVariable("menuId")Long menuId){
		menuService.removeMenu(menuId);
		return RestResponse.success();
	}
	
	@ApiOperation("修改菜单")
	@ApiImplicitParam(name = "menu", value = "菜单", required = true, dataType = "MenuDTO", paramType="body")
	@PutMapping(value = "/")
	public RestResponse<Nullable> modifyMenu(@RequestBody MenuDTO menu){
		menuService.modifyMenu(menu);
		return RestResponse.success();
	}
	
	@ApiOperation("查询单个菜单")
	@ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "Long", paramType="path")
	@GetMapping(value = "/{menuId}")
	public RestResponse<MenuDTO> queryMenu(@PathVariable("menuId")Long menuId){
		MenuDTO dto = menuService.queryMenu(menuId);
		return RestResponse.success(dto);
	}
	
	@ApiOperation("根据应用查询菜单")
	@ApiImplicitParam(name = "applicationCode", value = "菜单所属应用编码", required = true, dataType = "String", paramType="path")
	@GetMapping(value = "/applicationCode/{applicationCode}")
	public RestResponse<List<MenuDTO>> queryMenuByApplicationCode(@PathVariable("applicationCode")String applicationCode){
		List<MenuDTO> menuList = menuService.queryMenuByApplicationCode(applicationCode);
		return RestResponse.success(menuList);
	}
	
	
	@ApiOperation("条件检索菜单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "queryParams", value = "菜单查询参数",  dataType = "MenuQueryParams", paramType="body"),
		@ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType="query"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType="query")
	})
	@PostMapping(value = "/condition")
	public RestResponse<Page<MenuDTO>> queryMenu(@RequestBody MenuQueryParams queryParams, @RequestParam Integer pageNo, @RequestParam Integer pageSize){
		Page<MenuDTO> page = menuService.queryMenu(queryParams, pageNo, pageSize);
		return RestResponse.success(page);
	}
	
	
	

}
