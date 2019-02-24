package com.uiotsoft.micro.resource.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.resource.ResourceServer;
import com.uiotsoft.micro.resource.dto.MenuDTO;
import com.uiotsoft.micro.resource.dto.MenuQueryParams;

/**
 * @author 孔得峰
 * @date 2018年08月13日 11:03:33
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {ResourceServer.class})
public class MenuServiceTest {
	
	@Autowired
	private MenuService menuService;
	
	@Test
	public void testQueryMenu(){
		try {
			MenuQueryParams params = new MenuQueryParams();
			params.setApplicationCode("");
			int pageNo = 1;
			int pageSize = 10;
			Page<MenuDTO> page = menuService.queryMenu(params, pageNo, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
