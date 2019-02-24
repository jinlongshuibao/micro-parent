package com.uiotsoft.micro.user.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.uiotsoft.micro.user.entity.Tenant;

import io.swagger.annotations.ApiModel;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "TenantDTO", description = "租户信息")
public class TenantDTO {
	
	@ApiModelProperty("租户id")
	private Long id;
	
	@ApiModelProperty("租户名称")
	private String name;
	
	@ApiModelProperty("租户类型编码")
	private String tenantTypeCode;
	
	/**
	 * 将entity转为DTO
	 * @param entity
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月24日 下午5:23:30
	 */
	public static TenantDTO convertEntityToDto(Tenant entity){
		TenantDTO dto = new TenantDTO();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}

	/**
	 * 将entity列表转为DTO列表
	 * @param entitys
	 * @return
	 * @author 孔得峰
	 * @date 2018年7月24日 下午5:24:36
	 */
	public static List<TenantDTO> convertEntitysToDtos(List<Tenant> entitys){
		List<TenantDTO> dtos = new ArrayList<>();
		if(entitys != null && entitys.size()>0) {
			for(Tenant entity : entitys){
				dtos.add(convertEntityToDto(entity));
			}
		}
		return dtos;
	}
	/**
	 * 将entity转为DTO
	 * @param dto
	 * @return
	 * @author 孔得峰
	 * @date 2018年9月4日 下午4:49:24
	 */
	public static Tenant convertDtoToEntity(TenantDTO dto){
		Tenant entity = new Tenant();
		BeanUtils.copyProperties(dto, entity);
		return entity;
	}
}
