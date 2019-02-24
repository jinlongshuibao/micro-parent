package com.uiotsoft.micro.oauth2.domain;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class WdcyUserDetails implements UserDetails {

    private static final long serialVersionUID = 3957586021470480642L;

    /**
     * 用户的授权集合
     */
    protected List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    
    private String username;
    
    private String password;


    public WdcyUserDetails() {
    	initialAuthorities();
    }
    
    
    public WdcyUserDetails(String username, String password) {
    	this.username = username;
    	this.password = password;
    	initialAuthorities();
    }

 
    /**
     * 初始化用户角色,权限
     */
    private void initialAuthorities() {
        this.grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
    }

    /**
     * Return authorities, more information see {@link #initialAuthorities()}
     *
     * @return Collection of GrantedAuthority
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    
   
    /* 账户是否未过期 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*账户是否未锁定 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* 密码是否未过期 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*账户是否启用,默认true (启用)*/
    @Override
    public boolean isEnabled() {
        return true;
    }

    
}