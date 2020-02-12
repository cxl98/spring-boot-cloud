package com.cxl.cloud.common.config;

import com.cxl.cloud.entry.SysUser;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志（认证）拦截器
 */
public class SysInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            SysUser user = (SysUser) request.getSession().getAttribute("usr");
            if (user == null) {
                response.sendRedirect("/");
                return false;
            } else {
                return true;
            }
        }
        return true;

    }
    
}
