package com.uiotsoft.micro.user.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.uiotsoft.micro.user.entity.TenantTypeRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "TenantTypeRoleDTO", description = "租户类型角色信息")
@Data
public class TenantTypeRoleDTO {
	
	@ApiModelProperty("角色编码")
	private String roleCode;
	
	@ApiModelProperty("角色名称")
	private String roleName;
	
	@ApiModelProperty("角色所拥有的权限列表")
	private List<String> privilegeCodes = new ArrayList<>();
	
	public static TenantTypeRoleDTO convertEntityToDto(TenantTypeRole entity) {
		TenantTypeRoleDTO dto = new TenantTypeRoleDTO();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
	
	public static List<TenantTypeRoleDTO> convertEntityToDto(List<TenantTypeRole> list) {
		List<TenantTypeRoleDTO> dtoList = new ArrayList<>();
		if(list != null && list.size()>0) {
			for(TenantTypeRole role : list) {
				dtoList.add(convertEntityToDto(role));
			}
		}else {
			return null;
		}
		return dtoList;
	}
}
