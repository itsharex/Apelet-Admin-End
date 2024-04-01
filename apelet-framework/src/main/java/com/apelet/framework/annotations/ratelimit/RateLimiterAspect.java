package com.apelet.framework.annotations.ratelimit;

import com.apelet.framework.annotations.ratelimit.implementation.MapRateLimitChecker;
import com.apelet.framework.annotations.ratelimit.implementation.RedisRateLimitChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限流切面处理
 *
 * @author xiaoyuan-zs
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final RedisRateLimitChecker redisRateLimitChecker;

    private final MapRateLimitChecker mapRateLimitChecker;


    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimit rateLimiter) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        log.info("当前限流方法:" + method.toGenericString());

        switch (rateLimiter.cacheType()) {
            case Map:
                mapRateLimitChecker.check(rateLimiter);
                return;
            case REDIS:
            default:
                redisRateLimitChecker.check(rateLimiter);
        }

    }

}
