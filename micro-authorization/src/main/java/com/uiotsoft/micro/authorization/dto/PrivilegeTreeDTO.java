package com.uiotsoft.micro.authorization.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import com.uiotsoft.micro.authorization.entity.PrivilegeTreeEntity;

import lombok.Data;

/**
 * 权限树结构
 */
@Data
@ApiModel(value = "PrivilegeTreeDTO", description = "权限树信息")
public class PrivilegeTreeDTO {
	
	/**
	 * 权限组使用id,权限使用code，约束code不能为纯数值保证不重复
	 */
	@ApiModelProperty("节点唯一标识，权限组使用id，权限使用code，约束code不能为纯数值保证不重复。")
	private String id;

	@ApiModelProperty("节点名称")
	private String name;
	
	/**
	 * 一定是权限组id，权限没有结构
	 */
	@ApiModelProperty("父节点标识")
	private String parentId;//父级id

	@ApiModelProperty("排序")
	private Integer sort;// 同层排序,不排序都为0

	@ApiModelProperty("状态")
	private Integer status;//状态
	
	@ApiModelProperty("是否为权限组")
	private boolean isGroup = true; //显示名称
	
	@ApiModelProperty("子节点集合")
	private List<PrivilegeTreeDTO> children = new ArrayList<>();
	
	/**
	 * entity转dto
	 * @param entity
	 * @return PrivilegeTreeDTO
	 * @author 杨小波
	 * @date 2018年07月28日 18:20:08
	 */
	public static PrivilegeTreeDTO convertEntityToDTO(PrivilegeTreeEntity entity){
	    PrivilegeTreeDTO dto = new PrivilegeTreeDTO();
	    dto.setId(entity.getId());
	    dto.setName(entity.getName());
	    dto.setParentId(entity.getParentId());
	    dto.setSort(entity.getSort());
	    dto.setStatus(entity.getStatus());
	    dto.setGroup(entity.isGroup());
        return dto;
	}
	
	/**
	 * entitys转dtos
	 * @param entitys
	 * @return List<PrivilegeTreeDTO>
	 * @author 杨小波
	 * @date 2018年07月28日 18:27:43
	 */
	public static List<PrivilegeTreeDTO> convertEntitysToDTOs(List<PrivilegeTreeEntity> entitys){
	    List<PrivilegeTreeDTO> dtos = new ArrayList<>();
	    for(PrivilegeTreeEntity en : entitys){
	        dtos.add(PrivilegeTreeDTO.convertEntityToDTO(en));
	    }
	    return dtos;
	}
}
