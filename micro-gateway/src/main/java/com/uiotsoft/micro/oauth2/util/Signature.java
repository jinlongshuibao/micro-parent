/**
 * 
 */
package com.uiotsoft.micro.oauth2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 参数签名
 * @author Administrator
 *
 */
public class Signature {
	
	private static final Logger logger = LoggerFactory.getLogger(Signature.class);
    /**
     * 
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
    	sortedParams.remove("sign");
    	//sortedParams.remove("signType");
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (StringUtils.areNotEmpty(key, value)) {
                content.append( key + value);
            }
        }
        return content.toString();
    }
    /**
     * 根据签名类型获取签名的字符
     * @param content
     * @param appKey
     * @param secret
     * @param signType
     * @return
     */
    public static String getSign(String content,String secret,String signType){
    	if("MD5".equalsIgnoreCase(signType)){
    		String plainText = secret+content+secret;
    		return MD5Util.getMd5(plainText);
    	}
    	return null;
    }
    /**
     * 验证签名是否正确
     * @param params
     * @param secert
     * @return
     */
	public static boolean verifySign(Map<String, String> params, String secert) {
		String signType = Util.varFormat(params.get("signType"));
		String sign = Util.varFormat(params.get("sign"));
		String content = getSignContent(params);
		logger.info("client secert : {}", secert);
		logger.info("sign content : {}", content);
		String verifySign = getSign(content, secert, signType);
		logger.info("input verifySign : {}", sign);
		logger.info("generate verifySign : {}", verifySign);
		if(verifySign.equalsIgnoreCase(sign)){
			logger.info("verifySign success");
			return true;
		}else{
			logger.info("verifySign fail");
			return false;
		}
	}
}
