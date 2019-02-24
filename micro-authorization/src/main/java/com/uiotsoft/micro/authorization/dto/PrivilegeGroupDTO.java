package com.uiotsoft.micro.authorization.dto;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.authorization.entity.PrivilegeGroupEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "PrivilegeGroupDTO", description = "权限组信息")
public class PrivilegeGroupDTO {

	@ApiModelProperty("权限组id")
	private Long id;

	@ApiModelProperty("权限组父id")
	private Long parentId;

	@ApiModelProperty("权限组名称")
	private String name;

	@ApiModelProperty("排序")
	private Integer sort;
	
	/**
	 * dto转entity
	 * @param dto
	 * @return PrivilegeGroupEntity
	 * @author 杨小波
	 * @date 2018年07月25日 17:02:02
	 */
	public static PrivilegeGroupEntity convertDTOToEntity(PrivilegeGroupDTO dto){
	    PrivilegeGroupEntity entity = new PrivilegeGroupEntity();
	    
	    entity.setId(dto.getId());
	    entity.setName(dto.getName());
	    entity.setParentId(dto.getParentId());
	    entity.setSort(dto.getSort());
	    
	    return entity;
	}
	
	/**
	 * entity转dto
	 * @param entity
	 * @return PrivilegeGroupDTO
	 * @author 杨小波
	 * @date 2018年07月26日 10:32:51
	 */
	public static PrivilegeGroupDTO convertEntityToDTO(PrivilegeGroupEntity entity){
        PrivilegeGroupDTO dto = new PrivilegeGroupDTO();
        
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setParentId(entity.getParentId());
        dto.setSort(entity.getSort());
        
        return dto;
    }
	
	public static List<PrivilegeGroupDTO> convertEntitysToDTOs(List<PrivilegeGroupEntity> entitys){
        List<PrivilegeGroupDTO> dtoList = new ArrayList<>();
        if(entitys != null && entitys.size()>0) {
        	for(PrivilegeGroupEntity entity : entitys){
                PrivilegeGroupDTO dto = convertEntityToDTO(entity);
                dtoList.add(dto);
            }
        }
        return dtoList;
	}
}
