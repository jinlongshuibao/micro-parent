package com.uiotsoft.micro.resource.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.common.domain.PageRequestParams;
import com.uiotsoft.micro.resource.ResourceServer;
import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.entity.Application;

/**
 * @author 杨小波 
 * @date 2018年08月09日 11:03:33
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {ResourceServer.class})
public class ApplicationDaoTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationDaoTest.class);
	
	@Autowired
	private ApplicationDao applicationDao;
	
	@Test
    public void testPageApplicationByConditions(){
	    ApplicationQueryParams pageRequest = new ApplicationQueryParams();
	    
	    PageRequestParams params = PageRequestParams.of(1, 3);
	    List<Application> list = applicationDao.pageApplicationByConditions(pageRequest, params);
	    logger.info("**-** {}", list);
	}
	
	@Test
	public void testDeleteApplication(){
	    applicationDao.deleteApplication("app_code_123456");
	}
	
	@Test
	public void testUpdateApplication(){
	    ApplicationDTO app= new ApplicationDTO();
        app.setCode("app_code_update");
        app.setName("app_name_update");
        app.setId(5L);
        applicationDao.updateApplication(app);
	}
	
	@Test  
    public void testCreateApplication(){
        ApplicationDTO app= new ApplicationDTO();
        app.setCode("app_code_123456");
        app.setName("app_name_456798");
        applicationDao.insertApplication(app);
    }

}
