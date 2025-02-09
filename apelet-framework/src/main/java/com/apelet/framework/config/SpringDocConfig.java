package com.apelet.framework.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoyuan-zs
 * SpringDoc API文档相关配置
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI agileBootApi() {
        return new OpenAPI()
            .info(new Info().title("Apelet Admin后台管理系统")
                .description("Apelet Admin API 演示")
                .version("v1.0.0")
                .license(new License().name("MIT 3.0").url("https://github.com/xiaoyuan-zs/Apelet-Admin-End")))
            .externalDocs(new ExternalDocumentation()
                .description("Apelet Admin后台管理系统接口文档")
                .url("https://juejin.cn/column/7159946528827080734"));
    }

}
