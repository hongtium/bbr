package com.bbr.tools.net;

import com.bbr.tools.AppConfigs;
import com.bbr.tools.FastMapByChar;
import com.bbr.tools.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: chenjx
 * Date: 2011-6-11
 */
public class ServletUtil {
    private final static Log log = LogFactory.getLog(ServletUtil.class);

    /** 获得CGI参数的value，过滤 单引号|双引号|左尖号|百分号 字符  */
    public static String getString(HttpServletRequest req, String name) {
        if (name == null)  return null;
        String ret = req.getParameter(name);
        if(ret != null) {
            ret = ret.replaceAll("<", "&lt;");
            ret = ret.replaceAll("\"", "&quot;");
            ret = ret.replaceAll("'", "&#39;");
            ret = ret.replaceAll("%", "&#37;");
        }
        return ret;
    }

    /** 获得过滤掉HTML符号的干净的CGI参数   */
    public static String getStr(HttpServletRequest req, String name) {
        return getStr(req, name, false);
    }

    public static String getStr(HttpServletRequest req, String name,
                                boolean leaveBlank) {
        if (name == null)
            return null;
        if (CGI_PARAM_FILTER_MAP == null)
            CGI_PARAM_FILTER_MAP = FastMapByChar.compileMap(FILTER_CHAR, MAPDES_CHAR);
        String srcstr = req.getParameter(name);
        // to map filter html char ....
        if (srcstr == null)
            return "";
        StringBuffer des = new StringBuffer(srcstr.length());
        int i = 0;
        while (i < srcstr.length()) {
            char cur = srcstr.charAt(i);
            String mapedChar = (String) CGI_PARAM_FILTER_MAP.get(String.valueOf(cur));
            if (mapedChar == null)
                des.append(cur);
            else if (mapedChar.equals(" ")) {
                if (leaveBlank)
                    des.append(' ');
                // skip it
            } else
                des.append(mapedChar);
            i++;
        }
        return des.toString();
    }

    /** 获得int型 cgi参数值 */
    public static int getInt(HttpServletRequest req, String name) {
        String Vs = req.getParameter(name);
        try {
            return Integer.valueOf(Vs).intValue();
        } catch (Exception E) {
            return 0;
        }
    }
    /** 获得long型 cgi参数值 */
    public static long getLong(HttpServletRequest req, String name) {
        String Vs = req.getParameter(name);
        try {
            return Long.valueOf(Vs).longValue();
        } catch (Exception E) {
            return 0;
        }
    }

    public static double getDouble(HttpServletRequest req, String name) {
        String Vs = req.getParameter(name);
        try {
            return Double.parseDouble(Vs);
        } catch (Exception E) {
            return 0.00000;
        }
    }

    /** 获得过滤掉HTML符号的干净的CGI参数    */
    public static String getAjaxStr(HttpServletRequest req, String name) {
        String v = getString(req,name);
        //System.out.println("ToolKit.getAjaxStr" + name + "->" + v);
        if(v == null){
            return  null;
        }
        try{
            String dec = java.net.URLDecoder.decode(v, "utf-8");

            //System.out.println("ToolKit.getAjaxStr2" + name + "->" + dec);
            if(dec == null) return null;
            //System.out.println("ToolKit.getAjaxStr3" + name + "->" + dec);
            //System.out.println("ToolKit.getAjaxStr4" + name + "->" + filterEmbedTag(dec));
            return  filterEmbedTag(dec);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getAjaxStrLight(HttpServletRequest req, String name) {
        String v = getString(req,name);

        if(v == null){
            return  null;
        }
        try{
            String dec = java.net.URLDecoder.decode(v, "utf-8");
            if(dec == null) return null;

            return  dec.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Pattern[] P_SCRIPT = new Pattern[11];
    public static String[] S_SCRIPT = new String[11];
    static{
        P_SCRIPT[0] = Pattern.compile("script",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[0] ="tpircs" ;
        P_SCRIPT[1] = Pattern.compile("OBJECT",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[1] ="tcejbo" ;
        P_SCRIPT[2] = Pattern.compile("BASE",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[2] ="esab" ;
        P_SCRIPT[3] = Pattern.compile("src=//",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[3] ="nosrc" ;
        P_SCRIPT[4] = Pattern.compile("iframe",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[4] ="emafi" ;
        P_SCRIPT[5] = Pattern.compile("CURSOR",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[5] ="rosruc" ;
        P_SCRIPT[6] = Pattern.compile("<meta (.*)>",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[6] ="<atem>" ;
        P_SCRIPT[7] = Pattern.compile("onclick=",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[7] =" noclick=" ;
        P_SCRIPT[8] = Pattern.compile("\\[flash=1,1\\](.+?)\\[/flash\\]",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[8] ="" ;
        P_SCRIPT[9] = Pattern.compile("onupload=",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[9] =" noupload=" ;
        P_SCRIPT[10] = Pattern.compile("onerror=",Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        S_SCRIPT[10] =" noerror=" ;
    }

    public static String filterEmbedTag(String src) {
        if(src == null || src.length() < 5) return src;
        int amt = 0;
        for(int i=0;i<P_SCRIPT.length;i++){
            Matcher m = P_SCRIPT[i].matcher(src);

            while (m.find()) {
                amt++;
                System.out.println("Match \"" + m.group(0) + "\" at positions " + m.start() + "-" + (m.end() - 1));
                src = src.replace(m.group(), S_SCRIPT[i]);
                if(amt>9) return src;
            }
        }
        return src;
    }

    // ##-----------------------------------------------

    /**
     * 根据HttpServletRequest获得url
     *
     * @param request 请求
     * @return url
     */
    public static String getUrl(HttpServletRequest request) {
        String url = "http://" + request.getServerName()
                + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort())
                + request.getContextPath() + request.getServletPath();
        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            url = url + "?" + request.getQueryString();
        }
        return url;
    }

    /**
     * 获取request的URI (也就是URL的域名后的部分)
     */
    public static String getUri(HttpServletRequest req) {
        return req.getRequestURI();
    }

    /**
     * 根据HttpServletRequest获得访问ip
     *
     * @param request 请求
     * @return ip
     */
    public static String ip(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || "".equals(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.split(",")[0].trim().length() > 15) {
            return "";
        }
        return ip.split(",")[0].trim();
    }

    public static Cookie getCookie(HttpServletRequest request, String key) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals(key)) {
                return c;
            }
        }
        return null;
    }

    public static boolean setCookie(HttpServletResponse response, String name, String value, String domain, int time) {
        try {
            Cookie cookie = new Cookie(name, value);
            cookie.setPath("/");
            if (!domain.equals("localhost")) {
                cookie.setDomain(domain);
            }
            if (time > 0) {
                cookie.setMaxAge(time);
            }
            response.addCookie(cookie);
            return true;
        } catch (Exception e) {
            log.error("write cookie(" + name + ":" + value + ") error:" + e.getMessage());
            return false;
        }
    }

    public static boolean setCookie(HttpServletResponse response, String name, String value, int time) {
        return setCookie(response, name, value, AppConfigs.getInstance().get("domains.root"), time);
    }

    public static void deleteCookie(HttpServletResponse response, String name, String domain) {
        setCookie(response, name, "", domain, 0);
    }

    /*public static String getWebRoot() {
        ServletContext servletContext = ServletActionContext.getServletContext();
        String webRoot = null;
        if (servletContext != null) {
            webRoot = ServletActionContext.getServletContext().getRealPath("/").replaceAll("\\\\", "/");
            log.debug("====>>>>> The root path of web context is " + webRoot);
        } else {
            log.debug("====>>>>> There is no servlet context");
        }
        return webRoot;
    }

    public static String getClassesRoot() {
        String webRoot = getWebRoot();
        if (webRoot != null) {
            return webRoot + "/WEB-INF/classes";
        } else {
            return Thread.currentThread().getContextClassLoader().getResource("").getPath();
        }
    } */

    public static String getContextRoot() {
        String contextRoot = null;//getWebRoot();
        if (StringUtil.isEmpty(contextRoot)) {
            //log.debug("The web context is empty,try application context root.");
            URL contextUrl = Thread.currentThread().getContextClassLoader().getResource("");
            if (contextUrl != null) {
                contextRoot = contextUrl.getPath();
                log.debug("Find root path of application context success.The path is " + contextRoot);
            } else {
                log.debug("Bad luck. Find root path of application context failed. try to use context.root configs");
                contextRoot = AppConfigs.getInstance().get("context.root");
            }
        }
        return contextRoot;
    }


    public static final char[] FILTER_CHAR = "`~!#$^&*(){}[];:,<>?/'\\\"".toCharArray();
    public static final char[] MAPDES_CHAR = "                        ".toCharArray();
    public static HashMap<String,String> CGI_PARAM_FILTER_MAP = null;
}
