/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.uiotsoft.micro.oauth2.infrastructure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.uiotsoft.micro.oauth2.domain.OauthClientDetails;
import com.uiotsoft.micro.oauth2.domain.OauthRepository;
import com.uiotsoft.micro.oauth2.domain.Privilege;
import com.uiotsoft.micro.oauth2.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 2015/11/16
 *
 * @author Shengzhao Li
 */
@Repository("oauthRepositoryJdbc")
public class OauthRepositoryJdbc implements OauthRepository {



    private static OauthClientDetailsRowMapper oauthClientDetailsRowMapper = new OauthClientDetailsRowMapper();
    
    private static UserRowMapper userRowMapper = new UserRowMapper();


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public OauthClientDetails findOauthClientDetails(String clientId) {
        final String sql = " select * from oauth_client_details where  client_id = ? ";
        final List<OauthClientDetails> list = this.jdbcTemplate.query(sql, new Object[]{clientId}, oauthClientDetailsRowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<OauthClientDetails> findAllOauthClientDetails() {
        final String sql = " select * from oauth_client_details order by create_time desc ";
        return this.jdbcTemplate.query(sql, oauthClientDetailsRowMapper);
    }

    @Override
    public void updateOauthClientDetailsArchive(String clientId, boolean archive) {
        final String sql = " update oauth_client_details set archived = ? where client_id = ? ";
        this.jdbcTemplate.update(sql, archive, clientId);
    }

    @Override
    public void saveOauthClientDetails(final OauthClientDetails clientDetails) {
        final String sql = " insert into oauth_client_details(client_id,resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri," +
                " authorities,access_token_validity,refresh_token_validity,additional_information,trusted,autoapprove) values (?,?,?,?,?,?,?,?,?,?,?,?)";

        this.jdbcTemplate.update(sql, ps -> {
            ps.setString(1, clientDetails.clientId());
            ps.setString(2, clientDetails.resourceIds());

            ps.setString(3, clientDetails.clientSecret());
            ps.setString(4, clientDetails.scope());

            ps.setString(5, clientDetails.authorizedGrantTypes());
            ps.setString(6, clientDetails.webServerRedirectUri());

            ps.setString(7, clientDetails.authorities());
            ps.setObject(8, clientDetails.accessTokenValidity());

            ps.setObject(9, clientDetails.refreshTokenValidity());
            ps.setString(10, clientDetails.additionalInformation());

            ps.setBoolean(11, clientDetails.trusted());
            ps.setString(12, clientDetails.autoApprove());

        });
    }
    

    
	
  @Override
   // @Cacheable(value = USER_CACHE, key = "#username")
    public User findByUsername(String username) {
        System.err.println("***************findByUsername");
        final String sql = " select u.user_id as id, u.user_name as username,p.user_pwd as password from cps_app_user_info u\n" + 
                ",cps_user_to_pwd p\n" + 
                "where u.USER_ID = p.USER_ID \n" + 
                "and u.USER_NAME = ? " + 
                "and p.REG_SN_ID in(\n" + 
                "select t.REG_SN_ID from(\n" + 
                "select REG_SN_ID from  cps_user_to_sn where \n" + 
                "user_id in (select ue.user_id from cps_app_user_info ue where ue.USER_NAME =?) order by edit_date desc limit 1) t\n" + 
                "\n" + 
                ")  ";
        final List<User> list = this.jdbcTemplate.query(sql, new Object[]{username,username}, userRowMapper);

        User user = null;
        if (!list.isEmpty()) {
            user = list.get(0);
            user.privileges().addAll(findPrivileges(user.getId()));
        }

        return user;
    }

	private Collection<? extends Privilege> findPrivileges(int id) {
		List<Privilege> privileges = new ArrayList<>();
		return privileges;
	}
    
}
