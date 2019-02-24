package com.uiotsoft.micro.oauth2.domain;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.alibaba.fastjson.JSON;
import com.uiotsoft.micro.oauth2.service.OauthService;


public class CustomJdbcTokenStore extends JdbcTokenStore {
	
    private static final Logger logger = LoggerFactory.getLogger(CustomJdbcTokenStore.class);
    
    @Autowired
    private OauthService oauthService;
    
    @Autowired
    @Qualifier("accessTokenMQProducer")
    private DefaultMQProducer accessTokenMQProducer;

    public CustomJdbcTokenStore(DataSource dataSource) {
        super(dataSource);
    }


    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return super.readAccessToken(tokenValue);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        super.removeAccessToken(token);
    }
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication)  {
        
        //TODO: 通过username、tokenId、token参数调用某业务接口 或者 发布生成token消息，来绑定 用户与token的关系
    	
        super.storeAccessToken(token,authentication);
       
        try {
            //获取username
            Authentication userAuthentication = authentication.getUserAuthentication();
            logger.info("**--**userAuthentication:{}",  userAuthentication);
            //如果是授权码模式，则发送消息
            if(userAuthentication != null){
            	String username = null;
            	if(userAuthentication.getPrincipal() instanceof WdcyUserDetails){
            		WdcyUserDetails user = (WdcyUserDetails) userAuthentication.getPrincipal();
            		username = user.getUsername();
            	}else{
            		username = (String)userAuthentication.getPrincipal();
            	}
                //消息内容
                Map<String , Object> messageBody = new HashMap<>();
                messageBody.put("authorizedGrantType", authentication.getOAuth2Request().getGrantType());
                messageBody.put("tokenId",  super.extractTokenKey(token.getValue()));
                messageBody.put("token", token.getValue() );
                messageBody.put("username", username);
                //将消息转为json
                String messageJSON = JSON.toJSONString(messageBody);
                String message = new String(messageJSON.getBytes(), "utf-8");
                Message msg = new Message("AccessTokenTopic", "push", String.valueOf(System.currentTimeMillis()) ,  message.getBytes());
                logger.info("**--**要发送的消息：{}",  messageBody);
                //发送消息
                accessTokenMQProducer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("MQ成功发送消息: " + sendResult);
                    }
                    @Override
                    public void onException(Throwable e) {
                        e.printStackTrace();
                    }
                });
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//accessTokenMQProducer.shutdown();
		}
       /* Authentication authen = authentication.getUserAuthentication();
        if (authen!=null) {
            Object object =authen.getPrincipal();
            if (object!=null) {
               WdcyUserDetails wdcyUserDetails = (WdcyUserDetails) object;
                System.err.println(wdcyUserDetails.getUserId());
                String hostSn = oauthService.getHostSn(wdcyUserDetails.getUserId());
                oauthService.saveTokenHostSn(hostSn, token.getValue(),super.extractTokenKey(token.getValue()));
                System.err.println(token.getValue());
            }
        }*/
    }

    public OAuth2RefreshToken readRefreshToken(String token) {
        return super.readRefreshToken(token);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        super.removeRefreshToken(token);
    }

}
