package com.uiotsoft.micro.resource.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.common.util.VerifyUtil;
import com.uiotsoft.micro.common.domain.BusinessException;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.resource.dao.MenuDao;
import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.dto.MenuQueryParams;
import com.uiotsoft.micro.resource.entity.Menu;

/**
 * @author 孔得峰
 * @date 2018年7月30日 下午4:05:24
 * 
 */
@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Override
	public void createMenu(MenuDTO menuDTO) {
	    if(StringUtils.isBlank(menuDTO.getTitle())){
	        throw new BusinessException(ErrorCode.E_110108);
	    }
	    if(menuDTO.getSort() == null){
            throw new BusinessException(ErrorCode.E_110113);
        }
	    if(menuDTO.getSort() < 0 || menuDTO.getSort() > 20 ){
	        throw new BusinessException(ErrorCode.E_110115);
	    }
	    if(StringUtils.isBlank(menuDTO.getApplicationCode())){
            throw new BusinessException(ErrorCode.E_110114);
        }
	    if(menuDTO.getCode() != null && !VerifyUtil.verifyCharacter(menuDTO.getCode())){
	        throw new BusinessException(ErrorCode.E_110116);
	    }

        //去除首尾空格
	    menuDTO.setTitle(menuDTO.getTitle().trim());
	    if(menuDTO.getCode() != null){
	        menuDTO.setCode(menuDTO.getCode().trim());
	    }
	    if(menuDTO.getUrl() != null){
	        menuDTO.setUrl(menuDTO.getUrl().trim());
	    }
	    if(menuDTO.getIcon() != null){
	        menuDTO.setIcon(menuDTO.getIcon().trim());
	    }
	    
	    if(StringUtils.isNotBlank(menuDTO.getCode())){
	        List<Menu> menuList = menuDao.selectMenuByCode(menuDTO.getCode());
	        if(menuList != null && menuList.size() != 0){
	            throw new BusinessException(ErrorCode.E_110111);
	        }
	    }
	    
		menuDao.insertMenu(menuDTO);
	}

	@Override
	public void modifyMenu(MenuDTO menuDTO) {
	    if(StringUtils.isBlank(menuDTO.getTitle())){
            throw new BusinessException(ErrorCode.E_110108);
        }
	    if(menuDTO.getId() == null){
	        throw new BusinessException(ErrorCode.E_110109);
	    }
        if(menuDTO.getSort() == null){
            throw new BusinessException(ErrorCode.E_110113);
        }
        if(menuDTO.getSort() < 0 || menuDTO.getSort() > 20 ){
            throw new BusinessException(ErrorCode.E_110115);
        }
        if(StringUtils.isBlank(menuDTO.getApplicationCode())){
            throw new BusinessException(ErrorCode.E_110114);
        }

        //去除首尾空格
        menuDTO.setTitle(menuDTO.getTitle().trim());
        if(menuDTO.getUrl() != null){
            menuDTO.setUrl(menuDTO.getUrl().trim());
        }
        if(menuDTO.getIcon() != null){
            menuDTO.setIcon(menuDTO.getIcon().trim());
        }
		Integer count = menuDao.updateMenu(menuDTO);
		if(count == 0){
		    throw new BusinessException(ErrorCode.E_110118);
		}
	}

	@Override
	public void removeMenu(Long id) {
	    if(id == null){
	        throw new BusinessException(ErrorCode.E_110109);
	    }
	    //判断是否有子节点
	    List<Menu> parentMenu = menuDao.selectChildMenu(id);
	    if(parentMenu != null && parentMenu.size() != 0){
	        throw new BusinessException(ErrorCode.E_110110);
	    }
	    
		Integer count = menuDao.deleteMenu(id);
		if(count == 0){
            throw new BusinessException(ErrorCode.E_110118);
        }
	}

	@Override
	public MenuDTO queryMenu(Long id) {
	    if(id == null){
            throw new BusinessException(ErrorCode.E_110109);
        }
		Menu menu = menuDao.selectMenuById(id);
		if(menu == null || StringUtils.isBlank(menu.getTitle())){
		    throw new BusinessException(ErrorCode.E_110109);
		}
		return MenuDTO.convertEntityToDto(menu);
	}

	@Override
	public List<MenuDTO> queryMenuByApplicationCode(String applicationCode) {
	    if(StringUtils.isBlank(applicationCode)){
            throw new BusinessException(ErrorCode.E_110104);
        }
		List<Menu> menuList = menuDao.selectMenuByApplicationCode(applicationCode);
		if(menuList == null || menuList.isEmpty()){
		    throw new BusinessException(ErrorCode.E_110106);
		}
		return MenuDTO.convertEntityToDto(menuList);
	}

	@Override
	public Page<MenuDTO> queryMenu(MenuQueryParams params, Integer pageNo, Integer pageSize) {
		PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
		List<Menu> list = menuDao.pageMenuByConditions(params, pageRequest);
		Long total = menuDao.countAccountByConditions(params);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<MenuDTO> dtoList = MenuDTO.convertEntityToDto(list);
		Page<MenuDTO> page = new PageImpl<>(dtoList,pageable,total);
		return page;
	}

}
