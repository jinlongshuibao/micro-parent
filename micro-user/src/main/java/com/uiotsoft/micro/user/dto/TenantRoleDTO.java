package com.uiotsoft.micro.user.dto;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.user.entity.TenantRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "TenantRoleDTO", description = "租户内角色信息")
@Data
public class TenantRoleDTO {
	
	@ApiModelProperty("角色编码")
	private String code;
	
	@ApiModelProperty("角色名称")
	private String name;
	
	@ApiModelProperty("角色所拥有的权限列表")
	private List<String> privilegeCodes = new ArrayList<>();
	
	public static TenantRoleDTO convertEntityToDto(TenantRole entity) {
		TenantRoleDTO dto = new TenantRoleDTO();
		dto.setName(entity.getRoleName());
		dto.setCode(entity.getRoleCode());
		return dto;
	}
	
	public static List<TenantRoleDTO> convertEntityToDto(List<TenantRole> list) {
		List<TenantRoleDTO> dtoList = new ArrayList<>();
		if(list != null && list.size()>0) {
			for(TenantRole role : list) {
				dtoList.add(convertEntityToDto(role));
			}
		}else {
			return null;
		}
		return dtoList;
	}

}
