package com.uiotsoft.micro.user.common.cache;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uiotsoft.micro.user.UserServer;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest (classes = {UserServer.class})
public class CacheTest {
	
	@Autowired
	private Cache cache;
	
	@Test
	public void testSet() {
		cache.set("cache1111", "cache1111v");
	}
	
	
	
}
