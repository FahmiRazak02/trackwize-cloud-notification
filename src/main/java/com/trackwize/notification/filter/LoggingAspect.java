package com.trackwize.notification.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        String trackingId = MDC.get("trackingId");
        String userId = MDC.get("userId");
        String methodName = joinPoint.getSignature().toShortString();

        log.info("========== [START] {} ==========", methodName);
        if (StringUtils.isNotBlank(trackingId)){log.info("trackingId: {}", trackingId);}
        if (StringUtils.isNotBlank(userId)){log.info("userId: {}", userId);}

        Object result = joinPoint.proceed();

        log.info("========== [END] {}==========", methodName);

        return result;
    }

    @Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("========== [START] {} ==========", methodName);
        Object result = joinPoint.proceed();

        log.info("========== [END] {} ==========", methodName);
        return result;
    }
}
