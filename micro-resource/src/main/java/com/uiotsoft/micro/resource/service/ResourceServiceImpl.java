package com.uiotsoft.micro.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.uiotsoft.micro.common.domain.BusinessException;
import com.uiotsoft.micro.common.domain.ErrorCode;
import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.resource.dao.ApplicationDao;
import com.uiotsoft.micro.resource.dao.MenuDao;
import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.dto.ResourceDTO;
import com.uiotsoft.micro.resource.entity.Application;
import com.uiotsoft.micro.resource.entity.Menu;
import com.uiotsoft.micro.resource.loader.ResourceLoader;

@Transactional
@Service
public class ResourceServiceImpl implements ResourceService {
	
	@Autowired
	private ApplicationDao applicationDao;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private List<ResourceLoader> resourceLoaders;

	private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

	@Override
	public void createApplication(ApplicationDTO application) {
	    /*
	     * 参数校验
	     */
	    if(StringUtils.isBlank(application.getName())){
	        throw new BusinessException(ErrorCode.E_110103);
	    }
	    if(StringUtils.isBlank(application.getCode())){
	        throw new BusinessException(ErrorCode.E_110104);
	    }
        //去除首尾空格
	    application.setName(application.getName());
	    application.setCode(application.getCode());
	    
	    List<Application> appList = applicationDao.selectApplicationByCode(application.getCode());
	    if(appList.size() != 0){
	        throw new BusinessException(ErrorCode.E_110111);
	    }
	    List<Application> appListByName = applicationDao.selectApplicationByName(application.getName());
        if(appListByName.size() != 0){
            throw new BusinessException(ErrorCode.E_110119);
        }
	    
		applicationDao.insertApplication(application);
	}

	@Override
	public void modifyApplication(ApplicationDTO application) {
	    /*
         * 参数校验
         */
	    if(StringUtils.isBlank(application.getName())){
            throw new BusinessException(ErrorCode.E_110103);
        }if(StringUtils.isBlank(application.getCode())){
            throw new BusinessException(ErrorCode.E_110104);
        }
        //去除首尾空格
        application.setName(application.getName());
        application.setCode(application.getCode());
        
		Integer count = applicationDao.updateApplication(application);
		if(count == 0){
		    throw new BusinessException(ErrorCode.E_110117);
		}
	}

	@Override
	public ApplicationDTO queryApplication(String applicationCode) {
	    if(StringUtils.isBlank(applicationCode)){
	        throw new BusinessException(ErrorCode.E_110104);
	    }
		Application entity = applicationDao.selectByCode(applicationCode);
		if(entity == null || StringUtils.isBlank(entity.getName())){
		    throw new BusinessException(ErrorCode.E_110106);
		}
		ApplicationDTO dto = new ApplicationDTO();
		BeanUtils.copyProperties(entity, dto);
		return dto;
	}
	
	@Override
	public void removeApplication(String applicationCode) {
	    /*
         * 参数校验
         */
	    if(StringUtils.isBlank(applicationCode)){
	        throw new BusinessException(ErrorCode.E_110104);
	    }
	    
	    List<Menu> applications = menuDao.selectMenuByApplicationCode(applicationCode);
	    if(applications != null && applications.size() != 0){
	        throw new BusinessException(ErrorCode.E_110112);
	    }
	    
		Integer count = applicationDao.deleteApplication(applicationCode);
		if(count == 0){
            throw new BusinessException(ErrorCode.E_110117);
        }
	}

	@Override
	public Page<ApplicationDTO> pageApplicationByConditions(ApplicationQueryParams params, Integer pageNo, Integer pageSize) {
	    /*
         * 参数校验
         */
	    if(pageNo == null || pageNo < 1){
	        throw new BusinessException(ErrorCode.E_110105);
	    }
	    if(pageSize == null || pageSize < 1){
            throw new BusinessException(ErrorCode.E_110105);
        }
		PageRequestParams pageRequest = PageRequestParams.of(pageNo, pageSize);
		List<Application> list = applicationDao.pageApplicationByConditions(params, pageRequest);
		/*
		 * 处理分页
		 */
		Long total = applicationDao.countApplicationByConditions(params);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<ApplicationDTO> dtoList = null;
		if(list.size() != 0){
		    dtoList = ApplicationDTO.convertEntitysToDtos(list);
		}else{
		    dtoList = new ArrayList<>(); 
		}
		Page<ApplicationDTO> page = new PageImpl<>(dtoList,pageable,total);
		return page;
	}

	@Override
	public ResourceDTO loadResources(List<String> privileageCodes, String applicationCode) {
	    logger.info("[应用]{}[权限]{}",applicationCode, privileageCodes);
	    if(StringUtils.isBlank(applicationCode)){
	        throw new BusinessException(ErrorCode.E_110104);
	    }
		ResourceDTO resources  = new ResourceDTO();
		Application entity = applicationDao.selectByCode(applicationCode);
		logger.info("[查找到的应用]{}", entity);
		if(entity == null || StringUtils.isBlank(entity.getCode())){
		    logger.info("[查询应用为空]");
		    throw new BusinessException(ErrorCode.E_110106);
		}
		resources.setApplicationCode(entity.getCode());
		resources.setApplicationName(entity.getName());
		for(ResourceLoader resourceLoader : getResourceLoaders()){
			String resource = resourceLoader.load(privileageCodes, applicationCode);
			logger.info("[loader]>>{}", resource);
			resources.getAppRes( ).put(resourceLoader.getType(), JSONObject.parseObject(resource));
		}
		logger.info("[资源加载器，返回]{}", resources);
		return resources;
	}

	@Override
	public List<ResourceLoader> getResourceLoaders() {
		return resourceLoaders == null ? new ArrayList<ResourceLoader>() : resourceLoaders;
	}

}
