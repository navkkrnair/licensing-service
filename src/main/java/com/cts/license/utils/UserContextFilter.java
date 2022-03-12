package com.cts.license.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class UserContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        log.debug("Getting {} from header", UserContext.CORRELATION_ID);
        String correlationId = httpServletRequest.getHeader(UserContext.CORRELATION_ID);
        UserContextHolder.getContext()
                         .setCorrelationId(correlationId);

        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
