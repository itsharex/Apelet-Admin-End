package com.apelet.domain.system.config;

import com.apelet.common.core.page.PageDTO;
import com.apelet.domain.common.cache.CacheCenter;
import com.apelet.domain.system.config.command.ConfigUpdateCommand;
import com.apelet.domain.system.config.db.SysConfigEntity;
import com.apelet.domain.system.config.db.SysConfigService;
import com.apelet.domain.system.config.dto.ConfigDTO;
import com.apelet.domain.system.config.model.ConfigModel;
import com.apelet.domain.system.config.model.ConfigModelFactory;
import com.apelet.domain.system.config.query.ConfigQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyuan-zs
 */
@Service
@RequiredArgsConstructor
public class ConfigApplicationService {

    private final ConfigModelFactory configModelFactory;

    private final SysConfigService configService;

    public PageDTO<ConfigDTO> getConfigList(ConfigQuery query) {
        Page<SysConfigEntity> page = configService.page(query.toPage(), query.toQueryWrapper());
        List<ConfigDTO> records = page.getRecords().stream().map(ConfigDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public ConfigDTO getConfigInfo(Long id) {
        SysConfigEntity byId = configService.getById(id);
        return new ConfigDTO(byId);
    }

    public void updateConfig(ConfigUpdateCommand updateCommand) {
        ConfigModel configModel = configModelFactory.loadById(updateCommand.getConfigId());
        configModel.loadUpdateCommand(updateCommand);

        configModel.checkCanBeModify();

        configModel.updateById();

        CacheCenter.configCache.invalidate(configModel.getConfigKey());
    }


}
