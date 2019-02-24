package com.uiotsoft.micro.resource.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.resource.ResourceServer;
import com.uiotsoft.micro.resource.dto.ApplicationDTO;
import com.uiotsoft.micro.resource.dto.ApplicationQueryParams;
import com.uiotsoft.micro.resource.dto.ResourceDTO;

/**
 * @author 杨小波 修改
 * @date 2018年08月09日 11:03:33
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {ResourceServer.class})
public class ResourceServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ResourceServiceTest.class);
	
	@Autowired
	private ResourceService resourceService;
	
	@Test  
    public void testPageApplicationByConditions(){
	    ApplicationQueryParams query = new ApplicationQueryParams();
	    query.setName("test");
        Page<ApplicationDTO> page = resourceService.pageApplicationByConditions(query, 1, 2);
        List<ApplicationDTO> list = page.getContent();
        System.out.println("**--**  :" + list);
    }
	
	@Test
	public void testLoadResources(){
		
		List<String> privileageCodes = new ArrayList<>();
		//privileageCodes.add("admin_page_privilege");
		privileageCodes.add("admin_page_application");
		privileageCodes.add("admin_page_menu");
		privileageCodes.add("admin_page_tenantType");
		privileageCodes.add("admin_page_tenant");
		privileageCodes.add("admin_page_accessUser");
		privileageCodes.add("admin_page_appkey");
		privileageCodes.add("admin_page_role");
		privileageCodes.add("admin_page_account");
		ResourceDTO resources = resourceService.loadResources(privileageCodes , "UnifiedUserManagement");
		logger.info("ApplicationCode: {}   ApplicationName: {}   ", resources.getApplicationCode(), resources.getApplicationName());
		for(Entry<String, Object> entry : resources.getAppRes().entrySet()){
			logger.info("type-{}  :{} ", entry.getKey(), entry.getValue());
		}
		
	}
	

}
