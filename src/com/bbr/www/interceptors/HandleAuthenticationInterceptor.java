package com.bbr.www.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: xuhao
 * Date: 11-10-11
 */
public class HandleAuthenticationInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String queryStr = request.getQueryString();
        request.setAttribute("uri", uri);
        //窄顶导航的页面
        if (uri.startsWith("/user/")) {
            request.setAttribute("topnav", "narrow");
        }

        //先从Cookie中判断是否有用户信息
        String[] uinfo = {"0", "chenjx"};//UCookieCypher.getUserInfo(request);
        if (uinfo == null || uinfo[0] == null || "0".equals(uinfo[0])) {
            request.setAttribute("isLogin", false);
            //判断拦截的URI前缀 ...
            if (uri.startsWith("/user/")) {
                response.sendRedirect("http://x.idongmi.com/user/login.jsp");
            } else if (uri.startsWith("/hudong/set")) {
                response.getWriter().println("{\"s\":0,\"msg\":\"没有登录\"}");
                return false;
            } else if (uri.startsWith("/order/")) {
                response.getWriter().println("{\"s\":0,\"msg\":\"没有登录\"}");
                return false;
            }
            return true;
        } else {
            int uid = 0;
            try {
                uid = Integer.parseInt(uinfo[0]);
            } catch (Exception e) {
            }
            if (uid > 0) {
                request.setAttribute("isLogin", true);
                request.setAttribute("uid", uid);
                request.setAttribute("uname", uinfo[1]);
            } else {
                request.setAttribute("isLogin", false);
            }
        }
        return true;
    }
}
