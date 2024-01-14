package com.apelet.admin.customize.config;

import com.anji.captcha.properties.AjCaptchaProperties;
import com.anji.captcha.service.CaptchaCacheService;
import com.anji.captcha.service.impl.CaptchaServiceFactory;
import com.apelet.admin.customize.service.login.CaptchaSlideCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@Configuration
public class CaptchaSlideConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    @Primary
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config) {
        // 根据需求配置从哪里获取对应key值
        CaptchaCacheService cache = CaptchaServiceFactory.getCache(config.getCacheType().name());
        if (cache instanceof CaptchaSlideCacheServiceImpl) {
            ((CaptchaSlideCacheServiceImpl) cache).setStringRedisTemplate(stringRedisTemplate);
        }
        return cache;
    }

}
