package com.uiotsoft.micro.authorization.dto;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.authorization.entity.PrivilegeEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel(value = "PrivilegeDTO", description = "权限信息")
@Data
public class PrivilegeDTO  {
	
	@ApiModelProperty("权限id")
	private Long id;
	
	@ApiModelProperty("所属权限组id")
    private Long privilegeGroupId;

	@ApiModelProperty("权限编码")
    private String code;

	@ApiModelProperty("权限名称")
    private String name;
   
	@ApiModelProperty("状态")
    private Integer status;
	
	/**
	 * DTO转entity
	 * @param dto
	 * @return PrivilegeEntity
	 * @author 杨小波
	 * @date 2018年07月26日 15:41:38
	 */
	public static PrivilegeEntity 
	        convertDTOToEntity(PrivilegeDTO dto){
	    PrivilegeEntity entity = new PrivilegeEntity();
	    entity.setId(dto.getId());
	    entity.setCode(dto.getCode());
	    entity.setName(dto.getName());
	    entity.setPrivilegeGroupId(dto.getPrivilegeGroupId());
	    entity.setStatus(dto.getStatus());
	    
	    return entity;
	}
	
	/**
     * DTO转entity
     * @param dto
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年07月26日 15:41:38
     */
    public static PrivilegeDTO 
            convertEntityToDTO(PrivilegeEntity entity){
        PrivilegeDTO dto = new PrivilegeDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setPrivilegeGroupId(entity.getPrivilegeGroupId());
        dto.setStatus(entity.getStatus());
        
        return dto;
    }
    
    /**
     * DTO列表转entity列表
     * @param dto
     * @return PrivilegeEntity
     * @author 杨小波
     * @date 2018年07月26日 15:41:38
     */
    public static List<PrivilegeDTO> convertEntitysToDTOs(List<PrivilegeEntity> listEn){
    	List <PrivilegeDTO> listDTO = new ArrayList<>();
    	if(listEn != null && listEn.size()>0) {
    		for(PrivilegeEntity en : listEn){
                listDTO.add(convertEntityToDTO(en));
            }
    	}
        return listDTO;
    }
    
    public static List<PrivilegeEntity>
            convertDTOsToEntitys(List<PrivilegeDTO> listDTO){
        List <PrivilegeEntity> entityList = new ArrayList<>();
        for(PrivilegeDTO dto : listDTO){
            entityList.add(convertDTOToEntity(dto));
        }
        return entityList;
    }


}
