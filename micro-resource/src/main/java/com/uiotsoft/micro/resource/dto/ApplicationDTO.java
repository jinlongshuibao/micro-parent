package com.uiotsoft.micro.resource.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.uiotsoft.micro.resource.entity.Application;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "ApplicationDTO", description = "应用信息")
public class ApplicationDTO {


	@ApiModelProperty("应用id")
	private Long id;

	@ApiModelProperty("应用名称")
    private String name;

	@ApiModelProperty("应用编码")
    private String code;

	public static ApplicationDTO convertEntityToDto(Application entity) {
		ApplicationDTO dto = new ApplicationDTO();
		if(entity == null) {
			return null;
		}
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
	
	public static List<ApplicationDTO> convertEntitysToDtos(List<Application> list) {
		List<ApplicationDTO> dtoList = new ArrayList<>();
		if(list==null) {
			return dtoList;
		}
		for(Application application : list) {
			dtoList.add(convertEntityToDto(application));
		}
	
		return dtoList;
	}
    

     
}
