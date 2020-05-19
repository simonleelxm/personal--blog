package com.simon.bolg_sys.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandelInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("user");
        if(user==null){
            request.setAttribute("message","没有权限！请登录！");
            request.getRequestDispatcher("/admin").forward(request,response);
            return false;
        }else{
            return true;
        }

    }
}
