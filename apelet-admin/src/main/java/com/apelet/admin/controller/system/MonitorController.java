package com.apelet.admin.controller.system;

import com.apelet.admin.customize.aop.accessLog.AccessLog;
import com.apelet.common.core.base.BaseController;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.core.page.PageDTO;
import com.apelet.common.enums.common.BusinessTypeEnum;
import com.apelet.domain.common.cache.CacheCenter;
import com.apelet.domain.system.monitor.MonitorApplicationService;
import com.apelet.domain.system.monitor.dto.OnlineUserDTO;
import com.apelet.domain.system.monitor.dto.RedisCacheInfoDTO;
import com.apelet.domain.system.monitor.dto.ServerInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 缓存监控
 *
 * @author xiaoyuan-zs
 */
@Tag(name = "监控API", description = "监控相关信息")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController extends BaseController {

    private final MonitorApplicationService monitorApplicationService;

    @Operation(summary = "Redis信息")
    @PreAuthorize("@permission.has('monitor:cache:list')")
    @GetMapping("/cacheInfo")
    public ResponseDTO<RedisCacheInfoDTO> getRedisCacheInfo() {
        RedisCacheInfoDTO redisCacheInfo = monitorApplicationService.getRedisCacheInfo();
        return ResponseDTO.ok(redisCacheInfo);
    }


    @Operation(summary = "服务器信息")
    @PreAuthorize("@permission.has('monitor:server:list')")
    @GetMapping("/serverInfo")
    public ResponseDTO<ServerInfo> getServerInfo() {
        ServerInfo serverInfo = monitorApplicationService.getServerInfo();
        return ResponseDTO.ok(serverInfo);
    }

    /**
     * 获取在线用户列表
     *
     * @param ipAddress ip地址
     * @param username 用户名
     * @return 分页处理后的在线用户信息
     */
    @Operation(summary = "在线用户列表")
    @PreAuthorize("@permission.has('monitor:online:list')")
    @GetMapping("/onlineUsers")
    public ResponseDTO<PageDTO<OnlineUserDTO>> onlineUsers(String ipAddress, String username) {
        List<OnlineUserDTO> onlineUserList = monitorApplicationService.getOnlineUserList(username, ipAddress);
        return ResponseDTO.ok(new PageDTO<>(onlineUserList));
    }

    /**
     * 强退用户
     */
    @Operation(summary = "强退用户")
    @PreAuthorize("@permission.has('monitor:online:forceLogout')")
    @AccessLog(title = "在线用户", businessType = BusinessTypeEnum.FORCE_LOGOUT)
    @DeleteMapping("/onlineUser/{tokenId}")
    public ResponseDTO<Void> logoutOnlineUser(@PathVariable String tokenId) {
        CacheCenter.loginUserCache.delete(tokenId);
        return ResponseDTO.ok();
    }


}
