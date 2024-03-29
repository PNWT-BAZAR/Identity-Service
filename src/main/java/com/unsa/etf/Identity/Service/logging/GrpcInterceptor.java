package com.unsa.etf.Identity.Service.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class GrpcInterceptor implements HandlerInterceptor {
    private final GrpcSystemEvents grpcSystemEvents;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        grpcSystemEvents.logEvent("Identity service",
                "tkadric1",
                request.getMethod(),
                request.getRequestURI().split("/")[1],
                HttpStatus.valueOf(response.getStatus()).toString());

    }
}
