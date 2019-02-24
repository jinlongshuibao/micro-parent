package com.uiotsoft.micro.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author 赵庆旭
 * @version V1.0
 * (C) Copyright 河南紫光物联技术有限公司 Corporation 2018
 * All Rights Reserved.
 * @包名 com.uiot.community.mobile.utils.encrypt
 * @date 2018-10-24 16:47
 */
public class AES {
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";
    //秘钥KEY 目前测试支持16位和32位
    private static final String AES_APP_PRIVATE_KEY= "123456";
    //private static SecretKeySpec key = new SecretKeySpec(AES_APP_PRIVATE_KEY.getBytes(), ALGORITHM);


    public static void main(String[] args) {
    	String content = "13592443169";
        //加密
        System.out.println("加密前：" + content);
        String encryptResultStr = encrypt(content,"1234567812345678");
        System.out.println("加密后：" + encryptResultStr);
        //解密
        String decryptResult = decrypt(encryptResultStr,"1234567812345678");
        System.out.println("解密后：" + decryptResult);
    }
    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return string  返回加密内容String
     */
    public static String encrypt(String content,String key) {
        try {
            //如下注释代码key的转换方式Java单独使用正常，无法跨平台使用；
            //KeyGenerator kgen = KeyGenerator.getInstance("AES");
           // kgen.init(128, new SecureRandom(AES_APP_PRIVATE_KEY.getBytes()));
            //SecretKey secretKey = kgen.generateKey();
           // byte[] enCodeFormat = secretKey.getEncoded();
            //SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);// //"算法/模式/补码方式"
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));// 初始化
            byte[] result = cipher.doFinal(byteContent);

            return  parseByte2HexStr(result);//加密后字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**解密
     * @param encryptResultStr  待解密内容String
     * @return string  返回解密内容String
     */
    public static String decrypt(String encryptResultStr,String key) {
        try {
            byte[] decryptFrom = parseHexStr2Byte(encryptResultStr);
//            KeyGenerator kgen = KeyGenerator.getInstance("AES");
//            kgen.init(128, new SecureRandom(AES_APP_PRIVATE_KEY.getBytes()));
//            SecretKey secretKey = kgen.generateKey();
//            byte[] enCodeFormat = secretKey.getEncoded();
//            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);// //"算法/模式/补码方式"
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), ALGORITHM));// 初始化
            byte[] result = cipher.doFinal(decryptFrom);

            return new String(result); // 解密后字符串
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString().toLowerCase();
    }
    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
