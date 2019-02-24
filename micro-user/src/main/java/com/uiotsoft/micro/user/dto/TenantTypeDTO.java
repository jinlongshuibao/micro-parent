package com.uiotsoft.micro.user.dto;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.user.entity.TenantType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "TenantTypeDTO", description = "租户类型信息")
public class TenantTypeDTO {
	
	@ApiModelProperty("租户类型id")
	private Long id;
	
	@ApiModelProperty("租户类型编码")
	private String code;
	
	@ApiModelProperty("租户类型名称")
	private String name;
	
	@ApiModelProperty("租户类型权限范围")
	private List<TenantTypeRoleDTO> roles = new ArrayList<>(); 
	

    public static TenantType convertDtoToEntity(TenantTypeDTO dto) {
        TenantType entity = new TenantType();
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        return entity;
    }
	
	public static TenantTypeDTO convertEntityToDto(TenantType entity) {
		TenantTypeDTO dto = new TenantTypeDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setCode(entity.getCode());
		return dto;
	}
	
	public static List<TenantTypeDTO> convertEntityToDto(List<TenantType> list) {
		List<TenantTypeDTO> dtoList = new ArrayList<>();
		if(list != null && list.size()>0) {
			for(TenantType tenantType : list) {
				dtoList.add(convertEntityToDto(tenantType));
			}
		}else {
			return null;
		}
		return dtoList;
	}
}
