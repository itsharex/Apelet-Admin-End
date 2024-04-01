package com.apelet.framework.exception;


import cn.hutool.json.JSONUtil;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode.Internal;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


/**
 * 异常过滤器，因为配置的全局异常捕获器只能捕获MVC框架的异常
 * 不能捕获filter的异常
 * @author xiaoyuan-zs
 */
@Slf4j
@WebFilter(filterName = "ExceptionFilter", urlPatterns = "/*")
public class GlobalExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (ApiException ex) {
            log.error("global filter exceptions", ex);
            String resultJson = JSONUtil.toJsonStr(ResponseDTO.fail(ex));
            writeToResponse(response, resultJson);
        } catch (Exception e) {
            log.error("global filter exceptions, unknown error:", e);
            ResponseDTO<Object> responseDTO = ResponseDTO.fail(new ApiException(Internal.INTERNAL_ERROR, e.getMessage()));
            String resultJson = JSONUtil.toJsonStr(responseDTO);
            writeToResponse(response, resultJson);
        }
    }

    private void writeToResponse(ServletResponse response, String resultJson) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(resultJson);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }
}
