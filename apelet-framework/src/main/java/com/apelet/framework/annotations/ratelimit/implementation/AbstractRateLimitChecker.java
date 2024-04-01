package com.apelet.framework.annotations.ratelimit.implementation;

import com.apelet.framework.annotations.ratelimit.RateLimit;

/**
 * @author xiaoyuan-zs
 */
public abstract class AbstractRateLimitChecker {

    /**
     * 检查是否超出限流
     *
     * @param rateLimiter RateLimit
     */
    public abstract void check(RateLimit rateLimiter);

}
