package com.uiotsoft.micro.authorization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.authorization.entity.RoleEntity;

import lombok.Data;

@ApiModel(value = "RoleDTO", description = "角色信息")
@Data
public class RoleDTO  {
	
	@ApiModelProperty("角色id")
	private Long id;

	@ApiModelProperty("角色编码")
    private String code;

	@ApiModelProperty("角色名称")
    private String name;

	@ApiModelProperty("排序")
    private Integer sort;

	@ApiModelProperty("状态")
    private Integer status;
    
	@ApiModelProperty("角色所拥有的权限列表")
    private List<PrivilegeDTO> privileges = new ArrayList<>();
	
    
	/**
     * DTO转Entity
     * @author 杨小波
     */
    public static RoleEntity convertDTOToEntity(RoleDTO dto){
        RoleEntity entity = new RoleEntity();
        entity.setId(dto.getId());
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setSort(dto.getSort());
        entity.setStatus(dto.getStatus());
        entity.setPrivileges(PrivilegeDTO.convertDTOsToEntitys(dto.getPrivileges()));
        return entity;
    }
	
    /**
     * Entity转DTO
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年07月26日 15:41:38
     */
    public static RoleDTO convertEntityToDTO(RoleEntity entity){
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setSort(entity.getSort());
        dto.setStatus(entity.getStatus());
        dto.setPrivileges(PrivilegeDTO.convertEntitysToDTOs(entity.getPrivileges()));
        return dto;
    }
    
    
    /**
     * List<Entity>转List<DTO>
     * @param entityList
     * @return List<RoleDTO>
     * @author 杨小波
     * @date 2018年07月28日 10:26:49
     */
    public static List<RoleDTO> convertEntitysToDTOs(List<RoleEntity> entityList){
        List<RoleDTO> dtoList = new ArrayList<>();
        if(entityList != null && entityList.size()>0) {
        	for(RoleEntity role : entityList){
                RoleDTO dto = convertEntityToDTO(role);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }
}
