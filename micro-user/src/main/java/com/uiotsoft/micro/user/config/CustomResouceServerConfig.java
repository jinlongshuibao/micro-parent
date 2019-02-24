/*package com.uiotsoft.micro.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


@Configuration
@EnableResourceServer
public class CustomResouceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
    private TokenStore tokenStore;
	
	
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
        .tokenStore(tokenStore)
        .resourceId("user")
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	
        
    	http.httpBasic()
            .and()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/api")
                .access("#oauth2.hasScope('read')")
            .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
        ;
        
      
        
        
    }

}
*/