package com.uiotsoft.micro.api.resource.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 系统资源
 */
@Data
public class ResourceDTO  {
	
	String applicationCode;
	
	String applicationName;
	
	Map<String,Object> appRes = new HashMap<>(); //按资源类型分包括菜单等资源信息 如  menu:JSONObject
    
    
}
