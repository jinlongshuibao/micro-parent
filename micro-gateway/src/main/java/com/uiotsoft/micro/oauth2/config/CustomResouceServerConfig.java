package com.uiotsoft.micro.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class CustomResouceServerConfig {
	
	public static final String RESOURCE_ID = "resource-user";

	/**
	 * 用户服务
	 */
	@Configuration
	@EnableResourceServer
	public class UserResouceServerConfig extends
			ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
			// Since we want the protected resources to be accessible in the UI
			// as well we need
			// session creation to be allowed (it's disabled by default in
			// 2.0.6)
			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.and()
					.requestMatchers()
					.antMatchers("/user/**")
					.and()
					.authorizeRequests()
					.antMatchers("/user/**")
					.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_USER')");// 用户角色
																									// and
																									// hasRole('ROLE_USER')

		}

	}
	
	/**
	 * 授权服务
	 */
	@Configuration
	@EnableResourceServer
	public class AuthorizationResouceServerConfig extends
			ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
			// Since we want the protected resources to be accessible in the UI
			// as well we need
			// session creation to be allowed (it's disabled by default in
			// 2.0.6)
			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.and()
					.requestMatchers()
					.antMatchers("/authorization/**")
					.and()
					.authorizeRequests()
					.antMatchers("/authorization/**")
					.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_USER')");// 用户角色
																									// and
																									// hasRole('ROLE_USER')

		}

	}
	
	/**
	 * 资源服务
	 */
	@Configuration
	@EnableResourceServer
	public class ResourceResouceServerConfig extends
			ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
			// Since we want the protected resources to be accessible in the UI
			// as well we need
			// session creation to be allowed (it's disabled by default in
			// 2.0.6)
			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.and()
					.requestMatchers()
					.antMatchers("/resource/**")
					.and()
					.authorizeRequests()
					.antMatchers("/resource/**")
					.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_USER')");// 用户角色
																									// and
																									// hasRole('ROLE_USER')

		}

	}
	

	/**
	 * api服务
	 */
	@Configuration
	@EnableResourceServer
	public class ApiResouceServerConfig extends
			ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
			// Since we want the protected resources to be accessible in the UI
			// as well we need
			// session creation to be allowed (it's disabled by default in
			// 2.0.6)
			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.and()
					.requestMatchers()
					.antMatchers("/tw/**")
					.and()
					.authorizeRequests()
					.antMatchers("/tw/**")
					.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_API')");// 用户角色
																									// and
																									// hasRole('ROLE_USER')

		}

	}

	
	/**
	 * 家居服务
	 */
	@Configuration
	@EnableResourceServer
	public class SmartHomeResouceServerConfig extends
			ResourceServerConfigurerAdapter {

		@Autowired
		private TokenStore tokenStore;

		@Override
		public void configure(ResourceServerSecurityConfigurer resources)
				throws Exception {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
					.stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
			// Since we want the protected resources to be accessible in the UI
			// as well we need
			// session creation to be allowed (it's disabled by default in
			// 2.0.6)
			.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.and()
					.requestMatchers()
					.antMatchers("/smartHome/**")
					.and()
					.authorizeRequests()
					.antMatchers("/smartHome/**")
					.access("#oauth2.hasScope('read') and #oauth2.clientHasRole('ROLE_SMART_HOME')");// 用户角色
																									// and
																									// hasRole('ROLE_USER')

		}

	}
	

}
