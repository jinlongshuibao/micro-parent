package com.uiotsoft.micro.common.util;

import java.util.regex.Pattern;

/**  
 * @author 杨小波
 * @date 2018年09月21日 09:35:05  
 */
public class VerifyUtil {

    /**
     * <p>正则校验字符串是否正确的手机号</p>
     * <p>正则表达式内容：^((1[3-8][0-9])+\d{8})$</p>
     * <pre>
     * 若符合要求 = true
     * 不符合 = false
     * </pre>
     * @param str
     * @return boolean
     * @author 杨小波
     */
    public static boolean verifyPhoneNum(String str){
        String pattern = "^((1[3-9][0-9])+\\d{8})$";
        boolean matches = Pattern.matches(pattern, str);
        return matches;
    }
    
    /**
     * <p>校验字符串长度是否超过给定的长度</p>
     * <pre>
     * 超过 = false
     * 未超过 = true
     * </pre>
     * @param str 要校验的字符串
     * @param length 要校验的长度
     * @return boolean
     * @author 杨小波
     * @date 2018年09月21日 09:44:38
     */
    public static boolean verifyLength(String str, Integer length){
        if(str.length() > length){
            return false;
        }
        return true;
    }
    
    /**
     * <p>校验字符串长度是否小于给定的长度</p>
     * <pre>
     * 小于 = false
     * 不小于 = true
     * </pre>
     * @param str 要校验的字符串
     * @param length 要校验的长度
     * @return boolean
     * @author 杨小波
     * @date 2018年10月15日
     */
    public static boolean verifyMinLength(String str, Integer length){
        if(str.length() < length){
            return false;
        }
        return true;
    }
    
    /**
     * <p>正则校验字符串是否只能由字母、数字、中划线组成</p>
     * <p>正则表达式内容：^[A-Za-z0-9-_]*$</p>
     * <pre>
     * 若符合要求 = true
     * 不符合 = false
     * </pre>
     * @param str
     * @return boolean
     * @author 杨小波
     * @date 2018年09月21日 10:02:55
     */
    public static boolean verifyCharacter(String str){
        String pattern = "^[A-Za-z0-9-_]*$";
        boolean matches = Pattern.matches(pattern, str);
        return matches;
    }
    
 
}
