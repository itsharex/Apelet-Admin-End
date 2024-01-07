package com.apelet.domain.system.config.model;

import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import com.apelet.domain.system.config.db.SysConfigEntity;
import com.apelet.domain.system.config.db.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 配置模型工厂
 * @author valarchie
 */
@Component
@RequiredArgsConstructor
public class ConfigModelFactory {

    private final SysConfigService configService;

    public ConfigModel loadById(Long configId) {
        SysConfigEntity byId = configService.getById(configId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, configId, "参数配置");
        }
        return new ConfigModel(byId, configService);
    }

    public ConfigModel create() {
        return new ConfigModel(configService);
    }


}
