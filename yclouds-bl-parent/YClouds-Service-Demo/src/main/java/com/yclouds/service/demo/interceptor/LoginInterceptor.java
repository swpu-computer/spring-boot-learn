package com.yclouds.service.demo.interceptor;

import com.yclouds.common.core.error.code.BaseError;
import com.yclouds.common.core.response.ApiResp;
import com.yclouds.common.core.utils.JsonUtils;
import com.yclouds.common.core.utils.StringUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 *
 * @author yemeng-lhq
 * @version 2019/3/26 14:49
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws IOException {

        // PreFlight请求，忽略本拦截器
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        boolean flg = false; // 是否通过
        String token = request.getHeader("token");

        // 有token表示用户已登录（生产环境应该校验token合法性）
        if (StringUtils.isNotEmpty(token)) {
            flg = true;
        } else {
            // 根据系统需要，返回特定的消息格式
            ApiResp resp = ApiResp.retFail(BaseError.SYSTEM_NO_LOGIN);
            write(request, response, JsonUtils.toJson(resp));
        }
        return flg;
    }

    /**
     * 通过response返回错误信息给前端
     *
     * @param request 请求
     * @param response 响应
     * @param content 响应内容
     */
    private void write(HttpServletRequest request, HttpServletResponse response, String content)
        throws IOException {

        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(content);
    }
}
