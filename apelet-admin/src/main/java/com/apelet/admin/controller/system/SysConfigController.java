package com.apelet.admin.controller.system;

import com.apelet.admin.customize.aop.accessLog.AccessLog;
import com.apelet.common.core.base.BaseController;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.core.page.PageDTO;
import com.apelet.common.enums.common.BusinessTypeEnum;
import com.apelet.domain.common.cache.CacheCenter;
import com.apelet.domain.system.config.ConfigApplicationService;
import com.apelet.domain.system.config.command.ConfigUpdateCommand;
import com.apelet.domain.system.config.dto.ConfigDTO;
import com.apelet.domain.system.config.query.ConfigQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 参数配置 信息操作处理
 * @author valarchie
 */
@RestController
@RequestMapping("/system")
@Validated
@RequiredArgsConstructor
@Tag(name = "配置API", description = "配置相关的增删查改")
public class SysConfigController extends BaseController {

    private final ConfigApplicationService configApplicationService;

    /**
     * 获取参数配置列表
     */
    @Operation(summary = "参数列表", description = "分页获取配置参数列表")
    @PreAuthorize("@permission.has('system:config:list')")
    @GetMapping("/configs")
    public ResponseDTO<PageDTO<ConfigDTO>> list(ConfigQuery query) {
        PageDTO<ConfigDTO> page = configApplicationService.getConfigList(query);
        return ResponseDTO.ok(page);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@permission.has('system:config:query')")
    @GetMapping(value = "/config/{configId}")
    @Operation(summary = "配置信息", description = "配置的详细信息")
    public ResponseDTO<ConfigDTO> getInfo(@NotNull @Positive @PathVariable Long configId) {
        ConfigDTO config = configApplicationService.getConfigInfo(configId);
        return ResponseDTO.ok(config);
    }


    /**
     * 修改参数配置
     */
    @PreAuthorize("@permission.has('system:config:edit')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.MODIFY)
    @Operation(summary = "配置修改", description = "配置修改")
    @PutMapping(value = "/config/{configId}")
    public ResponseDTO<Void> edit(@NotNull @Positive @PathVariable Long configId, @RequestBody ConfigUpdateCommand config) {
        config.setConfigId(configId);
        configApplicationService.updateConfig(config);
        return ResponseDTO.ok();
    }

    /**
     * 刷新参数缓存
     */
    @Operation(summary = "刷新配置缓存")
    @PreAuthorize("@permission.has('system:config:remove')")
    @AccessLog(title = "参数管理", businessType = BusinessTypeEnum.CLEAN)
    @DeleteMapping("/configs/cache")
    public ResponseDTO<Void> refreshCache() {
        CacheCenter.configCache.invalidateAll();
        return ResponseDTO.ok();
    }
}
