package com.apelet.domain.system.config.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import com.apelet.domain.system.config.command.ConfigUpdateCommand;
import com.apelet.domain.system.config.db.SysConfigEntity;
import com.apelet.domain.system.config.db.SysConfigService;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigModel extends SysConfigEntity {

    private SysConfigService configService;

    private Set<String> configOptionSet;

    public ConfigModel(SysConfigService configService) {
        this.configService = configService;
    }

    public ConfigModel(SysConfigEntity entity, SysConfigService configService) {
        BeanUtil.copyProperties(entity, this);

        List<String> options =
            JSONUtil.isTypeJSONArray(entity.getConfigOptions()) ? JSONUtil.toList(entity.getConfigOptions(),
                String.class) : ListUtil.empty();

        this.configOptionSet = new HashSet<>(options);

        this.configService = configService;
    }

    public void loadUpdateCommand(ConfigUpdateCommand updateCommand) {
        this.setConfigValue(updateCommand.getConfigValue());
    }


    public void checkCanBeModify() {
        if (StrUtil.isBlank(getConfigValue())) {
            throw new ApiException(ErrorCode.Business.CONFIG_VALUE_IS_NOT_ALLOW_TO_EMPTY);
        }

        if (!configOptionSet.isEmpty() && !configOptionSet.contains(getConfigValue())) {
            throw new ApiException(ErrorCode.Business.CONFIG_VALUE_IS_NOT_IN_OPTIONS);
        }
    }

}
