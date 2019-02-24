package com.uiotsoft.micro.oauth2.service;


import java.util.List;

import com.uiotsoft.micro.oauth2.domain.OauthClientDetails;
import com.uiotsoft.micro.oauth2.dto.OauthClientDetailsDto;

/**
 * @author Shengzhao Li
 */

public interface OauthService {

    OauthClientDetails loadOauthClientDetails(String clientId);

    List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos();

    void archiveOauthClientDetails(String clientId);

    OauthClientDetailsDto loadOauthClientDetailsDto(String clientId);

    void registerClientDetails(OauthClientDetailsDto formDto);
    
}