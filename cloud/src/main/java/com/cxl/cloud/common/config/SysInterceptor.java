package com.cxl.cloud.common.config;

<<<<<<< HEAD
import com.cxl.cloud.entry.User;
=======
import com.cxl.cloud.entry.SysUser;
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
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
<<<<<<< HEAD
            User user = (User) request.getSession().getAttribute("user");
=======
            SysUser user = (SysUser) request.getSession().getAttribute("user");
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
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
