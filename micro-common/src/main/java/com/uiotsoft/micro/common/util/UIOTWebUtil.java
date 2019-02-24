package com.uiotsoft.micro.common.util;


import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Util.java此类用于web服务工具</p>
 * <p>@author:sxb</p>
 * <p>@date:2015-11-9</p>
 * <p>@remark:</p>
 */
public class UIOTWebUtil implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UIOTWebUtil.class);
   // private static Logger logger = LogManager.getLogger(new Util().getClass());

    public static final  int OK_TYPE = 0;//执行成功标志
    public static final  int ERROR_TYPE = 1;//执行失败标志
    public static final  String SN = "SN"; //家庭普通主机 》》都可以用FSN标示
    public static final  String SNR = "SNR";//家庭路由主机 》》都可以用FSN标示
    public static final  String SNH = "SNH";//酒店普通主机》》都可以用HSN标示
    public static final  String SNHR = "SNHR";//酒店路由主机》》都可以用HSN标示
    public static final  String FSN = "FSN";//家庭主机
    public static final  String HSN = "HSN";//酒店主机
    public static final  String SNX = "SNX";
    public static final  String SNAR = "SNAR";
    public static final  String USER = "user";//app帮助user
    public static final  String AIR_USER = "airBoxUser";//app帮助user
    public static final  int PAGE_SIZE = 10;//默认10页 
    public static final double DEF_PI = 3.14159265359; // PI
    public static final double DEF_2PI= 6.28318530712; // 2*PI
    public static final double DEF_PI180= 0.01745329252; // PI/180.0
    public static final double DEF_R =6370693.5; // radius of earth
    /**
     * 由于包含语法错误,当前请求无法被服务器理解..除非进行修改,否则客户端不应该重复提交这个请求..即错误请求 
     */
    /**
     * 请求已成功,请求所希望的响应头或数据体将随此响应返回..即服务器已成功处理了请求
     */
    public static final  int HTTP_CODE_200 = 200;//HTTP协议
    /**
     * 由于包含语法错误,当前请求无法被服务器理解..除非进行修改,否则客户端不应该重复提交这个请求..即错误请求
     */
    public static final  int HTTP_CODE_400 = 400;
    /**
     * 为授权，token过期等
     */
    public static final  int HTTP_CODE_401 = 401;
    /**
     * 用户重复注册/提交/绑定
     */
    public static final  int HTTP_CODE_402 = 402;
    /**
     * 参数验证不通过
     */
    public static final  int HTTP_CODE_403 = 403;
    /**
     * 找不到相应信息
     */
    public static final  int HTTP_CODE_404 = 404;
    /**
     * 类型不支持
     */
    public static final  int HTTP_CODE_405 = 405;
    /**
     * 服务器遇到了一个未曾预料的状况,导致其无法完成对请求的处理..一般来说,该问题都会在服务器的程序码出错时出现..即服务器内部错误
     */
    public static final  int HTTP_CODE_500 = 500;
    /**
     * 服务器不支持当前请求所需要的某个功能..当服务器无法识别请求的方法,且无法支持其对任何资源的请求时,可能返回此代码..即尚未实施
     */
    public static final  int HTTP_CODE_501 = 501;
    /**
     * 服务端异常
     */
    public static final  int HTTP_CODE_511 = 511;
    /**
     * 412  Precondition Failed 客户端请求信息的先决条件错误(本用在用户名重复不能注册)
     */
    public static final  int HTTP_CODE_412 = 412;
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 阿里天气预报
     */
    public static final String appkey = "23487890"; //APP KEY
    public static final String appsecret = "2a1510b14b97a98145a3c30c21de7bfb";// APP密钥
    
    /**
     * 根据主机类型 返回的是整体家庭主机还是酒店主机
     * @param snType
     * @return
     */
    public static String getWholeSnType(String snType){
        if (SN.equals(snType)||SNR.equals(snType)||SNX.equals(snType)||SNAR.equals(snType)) {
            return FSN;
        }else if (SNH.equals(snType)||SNHR.equals(snType)) {
            return HSN;
        }else {
            return FSN;
        }
    }
    public static String getOutTime(HourType hourType,int time) {
        Calendar calendar = Calendar.getInstance();
        switch (hourType) {
        case date:
            calendar.add(Calendar.DATE, time);
            break;
        case hour:
            calendar.add(Calendar.HOUR, time);
            break;
        case minute:
            calendar.add(Calendar.MINUTE, time);
            break;
        case second:
            calendar.add(Calendar.SECOND, time);
            break;
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int date = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year+""+month+""+date+""+hour+""+minute+""+second;
    }
    
    public static enum HourType{
        date,hour,minute,second;
    }

    
    /**
     * 按指定格式获得当前日期字符串
     * @param format 格式字符串
     */
    public static String getDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
        
    }

    /**
     * 按指定格式获得指定日期字符串
     * @param format 格式字符串
     * @param date 指定日期
     */
    public static String getDate(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String fdate = sdf.format(date);
        return sdf.format(date);
    }

    /**
     * 获得指定日期前面或后面某天的日期
     * @param date 日期
     * @param i 相差的天数(负值为前面的日期)
     */
    public Date relationDate(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.DATE, i);
        return cal.getTime();
    }

    /**
     * 转换日期字符串成日期对象
     * @param s 日期字符串
     * @throws ParseException 
     * @throws    Exception 如果发生异常
     */
    public Date parseDate(String s) throws ParseException  {
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT,
                                                   new Locale("zh", "CN"));
        return df.parse(s);
    }

    /**
     * 安全分隔字符串方法
     * @param s 字符串
     * @param regex 分隔正则表达式
     */
    public String[] split(String s, String regex) {
        return split(s, regex, -1);
    }

    /**
     * 安全分隔字符串方法
     * @param s 字符串
     * @param regex 分隔正则表达式
     * @param limit 限定起始下标
     */
    public String[] split(String s, String regex, int limit) {
        if (null == s) {
            return null;
        } else {
            return s.split(regex, limit);
        }
    }

    /**
     * 转换字符串为大写
     * @param s 字符串
     */
    public String toUpperCase(String s) {
        return varFormat(s).toUpperCase();
    }

    /**
     * 转换字符串为小写
     * @param s 字符串
     */
    public String toLowerCase(String s) {
        return varFormat(s).toLowerCase();
    }

    /**
     * 获取固定长度字符串
     * @param s 字符串
     * @param len 长度
     */
    public String limitFormat(String s, int len) {
        return limitFormat(s, len, true, "");
    }

    /**
     * 获取固定长度字符串
     * @param s 字符串
     * @param len 长度
     * @param c 后缀
     */
    public String limitFormat(String s, int len, String c) {
        return limitFormat(s, len, true, c);
    }

    /**
     * 获取固定长度字符串
     * @param s 字符串
     * @param len 长度
     * @param b 方向
     */
    public String limitFormat(String s, int len, boolean b) {
        return limitFormat(s, len, b, "");
    }

    /**
     * 获取固定长度字符串
     * @param s 字符串
     * @param len 长度
     * @param b 方向
     * @param c 前/后缀
     */
    public String limitFormat(String s, int len, boolean b, String c) {
        s = varFormat(s);
        c = varFormat(c);
        if(s.length() > len) {
            //需要截取
            if(b) {
                //从前向后截取
                if("".equals(c)) {
                    return (s.substring(0, len));
                } else {
                    //加后缀
                    return (s.substring(0, len)+c);
                }
            } else {
                //从后向前截取
                if("".equals(c)) {
                    return (s.substring(s.length()-len, s.length()));
                } else {
                    //加前缀
                    return (c+s.substring(s.length()-len, s.length()));
                }
            }
        } else {
            //不需要截取
            return (s);
        }
    }

    /**
     * 处理字符串符合SQL格式
     * @param s 字符串
     */
    public static String sqlFormat(String s) {
        s = varFormat(s);
        if(s.indexOf("'") !=-1){
            s = s.replaceAll("'", "''");
        }
        if(s.indexOf("%")!=-1){
            s =s.replaceAll("%", "\\\\%");
        }
        return s;
    }

    /**
     * 处理字符串符合URL格式
     * @param s 字符串
     */
    public static String urlFormat(String s) {
        return varFormat(s).replaceAll("&", "%26");
    }

    /**
     * 处理字符串符合XML格式
     * @param s 字符串
     * @param c 需要编码字符
     */
    public static String xmlFormat(String s, String c) {
        s = varFormat(s);
        c = varFormat(c);
        if (c.indexOf("&") != -1) {
            s = s.replaceAll("&", "&amp;");
        }
        if (c.indexOf("<") != -1) {
            s = s.replaceAll("<", "&lt;");
        }
        if (c.indexOf(">") != -1) {
            s = s.replaceAll(">", "&gt;");
        }
        if (c.indexOf("\"") != -1) {
            s = s.replaceAll("\"", "&quot;");
        }
        if (c.indexOf("'") != -1) {
            s = s.replaceAll("'", "&#39;");
        }
        if (c.indexOf(" ") != -1) {
            s = s.replaceAll(" ", "&nbsp;");
        }
        return s;
    }

    /**
     * 处理字符串符合XML格式
     * @param s 字符串
     */
    public static String xmlFormat(String s) {
        return xmlFormat(s, "<'&\">");
    }

    /**
     * 处理字符串符合XML格式且不生成空字符串
     * @param s 字符串
     * @param c 需要编码字符
     */
    public String xmlSpanFormat(String s, String c) {
        s = xmlFormat(s, c);
        if(s.equals("")) {
            s = "　";
        }
        return s;
    }

    /**
     * 处理字符串符合XML格式且不生成空字符串
     * @param s 字符串
     */
    public static String xmlSpanFormat(String s) {
        s = xmlFormat(s);
        if(s.equals("")) {
            s = "　";
        }
        return s;
    }

    /**
     * 处理字符串符合JavaScript的字符串格式
     * @param s 字符串
     */
    public String jsStringFormat(String s) {
        if(s == null) {
            s = "";
        } else {
            s = s.replaceAll("\\\\", "\\\\\\\\");
            s = s.replaceAll("\b", "\\\\b");
            s = s.replaceAll("\f", "\\\\f");
            s = s.replaceAll("\n", "\\\\n");
            s = s.replaceAll("\r", "\\\\r");
            s = s.replaceAll("\t", "\\\\t");
            s = s.replaceAll("'", "\\\\'");
            s = s.replaceAll("\"", "\\\\\"");
        }
        return s;
    }



    public Date relationMonth(Date date,int i){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.MONTH, i);
        return cal.getTime();
    }


    //implements Serializable
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException,IOException {
        ois.defaultReadObject();
    }


    public long getLong(String num){
        if(num==null) return 0;
        else{
            String pattern = "[0-9]+";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(num);
            boolean b = m.matches();
            if(b){
                return Long.parseLong(num);
            }
        }
        return 0;
    }
    public double getDouble(String num){
        if(num==null) return 0;
        else{
            String pattern = "[0-9]+(.[0-9]+)?";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(num);
            boolean b = m.matches();
            if(b){
                return Double.parseDouble(num);
            }
        }
        return 0;
    }

    public static boolean checkNum(String num){
        boolean b = false;
        if(num==null || varCheckEmp(num) || num.length() != 11) {

        }else{
            String pattern = "[0-9]+";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(num);
             b = m.matches();
        }
        return b;
    }

    public static Calendar getCalendar(){
        Calendar calendar = new GregorianCalendar();
        setCalendar(calendar);
        return calendar;
    }

    private static void setCalendar(Calendar calendar){
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(1);
    }

    public static Calendar getCalendar(int year, int month, int day_of_month){
        Calendar calendar = new GregorianCalendar(year,month,day_of_month);
        setCalendar(calendar);
        return calendar;
    }

    public static String htmlStringFormat(String text) {
          if(text==null || "".equals(text))
              return "";
          text = text.replace("<", "&lt;");
          text = text.replace(">", "&gt;");
          text = text.replace(" ", "&nbsp;");
          text = text.replace("\"", "&quot;");
          text = text.replace("\'", "&apos;");
          return text.replace("\n", "<br/>");
    }

    /**
     * 处理字符串空指针
     * @param s 字符串
     */
    public static boolean varCheckEmp(String s) {
        boolean b = false;
        if(s == null || "".equals(s.trim())) {
            b = true;
        }
        return b;
    }

    /**
     * 处理字符串空指针
     * @param s 字符串
     */
    public static String varFormat(String s) {
        if(s == null || "".equals(s.trim())) {
            s = "";
        }
        return s;
    }
    
    public static String varFormat(Object s) {
        if(s == null) {
            s = "";
        }
        return s.toString();
    }

       /**
     * 处理字符串空指针
     * @param s 字符串
     */
    public static String varFormat1(String s) {
        if(s == null || "".equals(s.trim())) {
            s = "　";
        }
        return s;
    }

    /**
    * 处理字符串空格
    * @param s 字符串
    */
   public static String varTrim(String s) {
       if(s == null) {
           s = "";
       } else {
           s = s.trim();
       }
       return s;
   }
   /**
     * 向HttpServletResponse中写数据,设置ContentType为html/txt;charset=utf-8
     * @param response
     * @param text 要写入的数据
     */
    public static void writeUtf8Text(HttpServletResponse response,String text){
        response.reset();
        //response 相关处理
        response.setContentType("html/text;charset=utf-8");
        //response.setCharacterEncoding("utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        try {
            response.getWriter().write(text);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            logger.error("修改数据流编码格式出错", e);
        }
    }
    /**
     * 向HttpServletResponse中写数据下载异常,设置ContentType为html/txt;charset=utf-8
     * @param response
     * @param text 要写入的数据
     */
    public static void writeUtf8TextDown(HttpServletResponse response,
            String objectTojson) {
         response.reset();
            //response 相关处理
            response.setContentType("html/text;charset=utf-8");
            //response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            System.out.println(response.getStatus());
            try {
                 /* int myCode=601;
                  response.setStatus(myCode);*/
                response.sendError(HTTP_CODE_404);
                response.getWriter().write(objectTojson);
                response.getWriter().flush();
                response.getWriter().close();
                //response.sendError(407, "Need authentication!!!" );
            } catch (IOException e) {
                logger.error("修改数据流编码格式出错", e);
            }
        
    }
    /**
      * 将SQLMAP结果列表转化成js数组
      */
    public static String getJsArrayFromList_2(List result) {
        
        StringBuffer sbf = new StringBuffer();
        sbf.append("[");
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                if (i == 0)
                    sbf.append("\"").append(result.get(i)).append("\"");
                else
                    sbf.append(",").append("\"").append(result.get(i)).append("\"");
            }
        }
        sbf.append("]");
        return sbf.toString();
    }
    
    public static Map<String, Object>rMap(int httpCode,String msg){
        Map<String, Object> rMap = new HashMap<String, Object>();
        rMap.put("httpCode", httpCode);
        rMap.put("dataResponse", msg);
        return rMap;
    }
       /**
     * 获取文件名的扩展名。
     * 例如 hn.txt的扩展名为txt；hn的扩展名为空字符串；
     *         hn...txt的扩展名为txt；h.n.txt的扩展名为txt；
     *        .txt的扩展名为txt
     * @param fileName
     * @return 如果文件名为null，则返回null。
     */
       public static String getExtensionName(String fileName){
            if(fileName==null) return null;
            
            String[] file = new String[2];
            file[0] = fileName.substring(0,fileName.lastIndexOf(".")<0?fileName.length():fileName.lastIndexOf("."));
            
            int subfix_start = fileName.lastIndexOf(".");
            if(subfix_start < 0){
                subfix_start = fileName.length();
            }else{
                subfix_start = subfix_start + 1;
            }
            int subfix_end = fileName.length();
            file[1] = fileName.substring(subfix_start,subfix_end);
            
            return file[1];
        }
       /**
        * MD5加密，可返回32位、16位根据需要调整
        * @param sourceStr
        * @return
        */
       public static String strToMd5(String sourceStr) {
           String result = "";
           try {
               MessageDigest md = MessageDigest.getInstance("MD5");
               md.update(sourceStr.getBytes());
               byte b[] = md.digest();
               int i;
               StringBuffer buf = new StringBuffer("");
               for (int offset = 0; offset < b.length; offset++) {
                   i = b[offset];
                   if (i < 0)
                       i += 256;
                   if (i < 16)
                       buf.append("0");
                   buf.append(Integer.toHexString(i));
               }
               result = buf.toString();
               //System.out.println("MD5(" + sourceStr + ",32) = " + result);
               //System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
           } catch (NoSuchAlgorithmException e) {
               logger.error("MD5加密出错32-16", e);
           }
           return result;
       }
       /**
        * MD5加密，可返回32位、16位根据需要调整
        * @param sourceStr
        * @return
        */
       public static String strToMd5Spt(String sourceStr) {
           String result = "";
           try {
               MessageDigest md = MessageDigest.getInstance("MD5");
               md.update(sourceStr.getBytes());
               byte b[] = md.digest();
               int i;
               StringBuffer buf = new StringBuffer("");
               for (int offset = 0; offset < b.length; offset++) {
                   i = b[offset];
                   if (i < 0)
                       i += 256;
                   if (i < 16)
                       buf.append("0");
                   buf.append(Integer.toHexString(i));
               }
               result = buf.toString();
               System.out.println(result);
               result = result.substring(16, 32);
           } catch (NoSuchAlgorithmException e) {
               logger.error("MD5加密出错32-16", e);
           }
           return result;
       }
       /** 
        *  
        * @param latitue 待测点的纬度 
        * @param longitude 待测点的经度 
        * @param areaLatitude1 纬度范围限制1 
        * @param areaLatitude2 纬度范围限制2 
        * @param areaLongitude1 经度限制范围1 
        * @param areaLongitude2 经度范围限制2 
        * @return 
        */  
       public static boolean isInArea(double latitue,double longitude,double areaLatitude1,double areaLatitude2,double areaLongitude1,double areaLongitude2){  
           if(isInRange(latitue, areaLatitude1, areaLatitude2)){//如果在纬度的范围内  
               if(areaLongitude1*areaLongitude2>0){//如果都在东半球或者都在西半球  
                   if(isInRange(longitude, areaLongitude1, areaLongitude2)){  
                       return true;   
                   }else {  
                       return false;  
                   }  
               }else {//如果一个在东半球，一个在西半球  
              /*    System.out.println("??????????");
                System.out.println(Math.abs(areaLongitude1));
                System.out.println(Math.abs(areaLongitude2));*/
                   if(Math.abs(areaLongitude1)+Math.abs(areaLongitude2)<180){//如果跨越0度经线在半圆的范围内  
                       if(isInRange(longitude, areaLongitude1, areaLongitude2)){  
                           return true;  
                       }else {  
                           return false;  
                       }  
                   }else{//如果跨越180度经线在半圆范围内  
                 /*     System.out.println("!!4"+Math.max(areaLongitude1, areaLongitude2));
                    System.out.println("!!5"+Math.min(areaLongitude1, areaLongitude2));*/
                       double left = Math.max(areaLongitude1, areaLongitude2);//东半球的经度范围left-180  
                       double right = Math.min(areaLongitude1, areaLongitude2);//西半球的经度范围right-（-180）  
                       if(isInRange(longitude, left, 180)||isInRange(longitude, 0, right)){  
                           return true;  
                       }else {  
                           return false;  
                       }  
                   }  
               }  
           }else{  
               return false;  
           }  
   }
       public static boolean isInRange(double point, double left,double right){  
      /*    System.out.println(point+"?"+left+"?"+right);
        System.out.println(Math.min(left, right));
        System.out.println(Math.max(left, right));*/
         if(point>=Math.min(left, right)&&point<=Math.max(left, right)){  
                return true;  
            }else {  
                return false;  
            }  
    }   
      
      
               //适用于近距离
       public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
       {
           double ew1, ns1, ew2, ns2;
           double dx, dy, dew;
           double distance;
           // 角度转换为弧度
           ew1 = lon1 * DEF_PI180;
           ns1 = lat1 * DEF_PI180;
           ew2 = lon2 * DEF_PI180;
           ns2 = lat2 * DEF_PI180;
           // 经度差
           dew = ew1 - ew2;
           // 若跨东经和西经180 度，进行调整
           if (dew > DEF_PI){
            dew = DEF_2PI - dew;
           } else if (dew < -DEF_PI){
             dew = DEF_2PI + dew;
           }
           dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
           dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
           // 勾股定理求斜边长
           distance = Math.sqrt(dx * dx + dy * dy);
           return distance;
       }
               //适用于远距离
       public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
       {
           double ew1, ns1, ew2, ns2;
           double distance;
           // 角度转换为弧度
           ew1 = lon1 * DEF_PI180;
           ns1 = lat1 * DEF_PI180;
           ew2 = lon2 * DEF_PI180;
           ns2 = lat2 * DEF_PI180;
           // 求大圆劣弧与球心所夹的角(弧度)
           distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
           // 调整到[-1..1]范围内，避免溢出
           if (distance > 1.0)
                distance = 1.0;
           else if (distance < -1.0)
                 distance = -1.0;
           // 求大圆劣弧长度
           distance = DEF_R * Math.acos(distance);
           return distance;
       }   
       public static Map<String, Object> validateParameters(Map<String, Object> paramMap,String param[]){
           List<String> list=new ArrayList<>();
           Map<String, Object> rMap = new HashMap<String, Object>();
           for(String key : paramMap.keySet()){ 
               list.add(key);
           }
           for(String value:param){
               while(!list.contains(value)){
                   Map<String,Object> msgMap = new HashMap<String, Object>();
                   rMap.put("httpCode", UIOTWebUtil.HTTP_CODE_403);
                   msgMap.put("msg", "获取参数不正确"+value);
                   rMap.put("errorResponse", msgMap);
                   return rMap;
               }
           }
           rMap.put("httpCode", HTTP_CODE_200);
           return rMap;
       }
        public static String subSnType(String snType) {
            snType=snType.substring(snType.length()-1);
            snType=snType.toUpperCase();
            return snType;
        }
        public static String getDType(String dType) {
             String sid[] = dType.split(",");
             String newDtype ="";
             int[] arrayDtype = new int[sid.length];
             for(int i = 0; i < arrayDtype.length; i++)
             {
                 arrayDtype[i] = Integer.parseInt(sid[i]);
             }
             Arrays.sort(arrayDtype);  //进行排序   
             for(int j: arrayDtype){   
                 newDtype += "," + String.valueOf(j);
            }  
             newDtype = newDtype.replaceFirst(",", "");
             return newDtype;
        }
       public static void main(String[] args) {
          /* Map<String, Object> paramMap = new HashMap<String, Object>();
           paramMap.put("SN", "SN1");
           paramMap.put("SI", "SI1");
           paramMap.put("userName", "userName1");
           String param[] = {"SN", "userName","phone"};
           //System.out.println(Util.validateParameters(paramMap, param));
           System.out.println(Util.subSnType("SNwfn"));*/
           //System.out.println(strToMd5("34-97-F6-53-35-14"));
           /*String SN="1811h86994e34dc89a08f17f44csz152";
           String dd=SN.substring(SN.length()-6);
           System.out.println(dd);
           System.out.println(SN.substring(26, 32));
           System.out.println(SN.substring(16, 32));
         
           String aString="V3.01";
           String d[]=aString.split("\\.");
           System.out.println(d.length);
           if("".equals(Util.varFormat(d[2]))){
               System.out.println("00");
           }*/
           
           List<String> list = new ArrayList<String>();
                list.add("abc");
                list.add("123");
                list.add("aaa");
                int index = list.indexOf("aaa");
                System.out.println(index);
                if(index != -1){
                    System.out.println(1);
                }
           
       }
    

       
       /** 
        * 将map转换成url 
        * @param map 
        * @return 
        */  
       public static String getUrlParamsByMap(Map<String, Object> map) {  
           if (map == null) {  
               return "";  
           }  
           StringBuffer sb = new StringBuffer();  
           for (Map.Entry<String, Object> entry : map.entrySet()) {  
               sb.append(entry.getKey() + "=" + entry.getValue());  
               sb.append("&");  
           }  
           String s = sb.toString();  
           if (s.endsWith("&")) {  
               s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");  
           }  
           return s;  
       }  
       
}
