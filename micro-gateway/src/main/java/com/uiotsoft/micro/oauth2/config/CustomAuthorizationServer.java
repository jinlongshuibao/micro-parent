package com.uiotsoft.micro.oauth2.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.uiotsoft.micro.oauth2.domain.CustomJdbcClientDetailsService;
import com.uiotsoft.micro.oauth2.domain.CustomJdbcTokenStore;
import com.uiotsoft.micro.oauth2.service.OauthService;


@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServer extends
		AuthorizationServerConfigurerAdapter {

	@Autowired
	private TokenStore tokenStore;
	

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private OauthService oauthService;

	@Autowired
	private AuthenticationManager authenticationManager;

	/*
	 * @Autowired private RedisConnectionFactory connectionFactory;
	 */
   

    @Bean
    public TokenStore tokenStore(DataSource dataSource) {
        return new CustomJdbcTokenStore(dataSource);
    }
    
    
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        return new CustomJdbcClientDetailsService(dataSource);
    }
    
    @Bean
   	public AuthorizationServerTokenServices tokenService() {
       	DefaultTokenServices service=new DefaultTokenServices();
       	service.setClientDetailsService(clientDetailsService);
       	service.setSupportRefreshToken(true);
   		service.setTokenStore(tokenStore);
   		service.setTokenEnhancer(tokenEnhancer());
   		service.setAccessTokenValiditySeconds(2592000);
   		service.setRefreshTokenValiditySeconds(8640000);
   		return service;
    }
    
    
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }
    
    @Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		 clients.withClientDetails(clientDetailsService);
	}
  
    
    @Bean
    public OAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(clientDetailsService);
    }
    
    
    
    @Bean
    public UserApprovalHandler userApprovalHandler() {
        OauthUserApprovalHandler userApprovalHandler = new OauthUserApprovalHandler();
        userApprovalHandler.setOauthService(oauthService);
        userApprovalHandler.setTokenStore(tokenStore);
        userApprovalHandler.setClientDetailsService(this.clientDetailsService);
        userApprovalHandler.setRequestFactory(oAuth2RequestFactory());
        return userApprovalHandler;
    }

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.authenticationManager(authenticationManager)
				//.userDetailsService(userDetailsService)// 若无，refresh_token会有UserDetailsService	 is required错误
				.authorizationCodeServices(authorizationCodeServices)
				.userApprovalHandler(userApprovalHandler())
				.tokenServices(tokenService())
				.pathMapping("/oauth/confirm_access", "/confirm_access")
				.pathMapping("/oauth/error", "/oauth_error")
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
	}
	
	@Bean
    public TokenEnhancer tokenEnhancer(){
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                if (accessToken instanceof DefaultOAuth2AccessToken){
                    DefaultOAuth2AccessToken token= (DefaultOAuth2AccessToken) accessToken;
                    Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
                    additionalInformation.put("openid","xxxxxxxxx");
                    token.setAdditionalInformation(additionalInformation);
                }
                return accessToken;
            }
        };
    }

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security)
			throws Exception {
		security
		.tokenKeyAccess("permitAll()")
		.checkTokenAccess("permitAll()")
		.allowFormAuthenticationForClients()//允许表单认证
		;
	}

	
}
