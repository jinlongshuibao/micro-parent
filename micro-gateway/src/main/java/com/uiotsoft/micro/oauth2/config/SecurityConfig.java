package com.uiotsoft.micro.oauth2.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*@Autowired
    private RemoteServiceAuthenticationProvider authenticationProvider;*/
	
	
	
	@Autowired
	private UserDetailsService userDetailsService;
   
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService);
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    //不定义没有password grant_type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/webjars/**", "/v2/**", "/swagger**", "/static/**", "/resources/**");
		//web.httpFirewall(new DefaultHttpFirewall());//StrictHttpFirewall 去除验url非法验证防火墙
		
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        
        
        
        
		http.authorizeRequests()
        .antMatchers("/login*").permitAll()
        .antMatchers("/main*").permitAll()
        .antMatchers("/index*").permitAll()
        .antMatchers("/oauth/rest_token*").permitAll()
        .antMatchers("/oauth/check_token*").permitAll()
        .antMatchers("/hi").permitAll()
       // .antMatchers("/useradmin/**").hasAnyRole("ADMIN") 普通web权限
        .antMatchers(HttpMethod.GET, "/login*").anonymous()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login.do")
        .failureUrl("/login?authentication_error=1")
        .defaultSuccessUrl("/oauth/authorize")
        .usernameParameter("username")
        .passwordParameter("password")
        .and()
        .logout()
        .logoutUrl("/logout.do")
        .logoutSuccessUrl("/oauth/authorize")
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("/")
        .and()
        .csrf().disable()
        .exceptionHandling()
        .accessDeniedPage("/login?authorization_error=2")
        .and().requestCache().requestCache(getRequestCache(http));

    }
    
    
    private RequestCache getRequestCache(HttpSecurity http) {
		RequestCache result = http.getSharedObject(RequestCache.class);
		if (result != null) {
			return result;
		}
		HttpSessionRequestCache defaultCache = new HttpSessionRequestCache();
		defaultCache.setRequestMatcher(createDefaultSavedRequestMatcher(http));
		return defaultCache;
	}

	@SuppressWarnings("unchecked")
	private RequestMatcher createDefaultSavedRequestMatcher(HttpSecurity http) {
		ContentNegotiationStrategy contentNegotiationStrategy = http
				.getSharedObject(ContentNegotiationStrategy.class);
		if (contentNegotiationStrategy == null) {
			contentNegotiationStrategy = new HeaderContentNegotiationStrategy();
		}

		RequestMatcher notFavIcon = new NegatedRequestMatcher(new AntPathRequestMatcher(
				"/**/favicon.ico"));

		MediaTypeRequestMatcher jsonRequest = new MediaTypeRequestMatcher(
				contentNegotiationStrategy, MediaType.APPLICATION_JSON);
		jsonRequest.setIgnoredMediaTypes(Collections.singleton(MediaType.ALL));
		RequestMatcher notJson = new NegatedRequestMatcher(jsonRequest);

		RequestMatcher notXRequestedWith = new NegatedRequestMatcher(
				new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));

		boolean isCsrfEnabled = http.getConfigurer(CsrfConfigurer.class) != null;

		List<RequestMatcher> matchers = new ArrayList<>();
		if (isCsrfEnabled) {
			RequestMatcher getRequests = new AntPathRequestMatcher("/**", "GET");
			matchers.add(0, getRequests);
		}
		matchers.add(notFavIcon);
		matchers.add(notJson);
		//matchers.add(notXRequestedWith);

		return new AndRequestMatcher(matchers);
	}

}
