package com.apelet.domain.system.log;

import com.apelet.common.core.page.PageDTO;
import com.apelet.domain.common.command.BulkOperationCommand;
import com.apelet.domain.system.log.db.SysLoginInfoEntity;
import com.apelet.domain.system.log.db.SysLoginInfoService;
import com.apelet.domain.system.log.db.SysOperationLogEntity;
import com.apelet.domain.system.log.db.SysOperationLogService;
import com.apelet.domain.system.log.dto.LoginLogDTO;
import com.apelet.domain.system.log.dto.OperationLogDTO;
import com.apelet.domain.system.log.dto.OperationLogQuery;
import com.apelet.domain.system.log.query.LoginLogQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class LogApplicationService {

    // TODO 命名到时候统一改成叫LoginLog
    private final SysLoginInfoService loginInfoService;

    private final SysOperationLogService operationLogService;

    public PageDTO<LoginLogDTO> getLoginInfoList(LoginLogQuery query) {
        Page<SysLoginInfoEntity> page = loginInfoService.page(query.toPage(), query.toQueryWrapper());
        List<LoginLogDTO> records = page.getRecords().stream().map(LoginLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public void deleteLoginInfo(BulkOperationCommand<Long> deleteCommand) {
        QueryWrapper<SysLoginInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("info_id", deleteCommand.getIds());
        loginInfoService.remove(queryWrapper);
    }

    public PageDTO<OperationLogDTO> getOperationLogList(OperationLogQuery query) {
        Page<SysOperationLogEntity> page = operationLogService.page(query.toPage(), query.toQueryWrapper());
        List<OperationLogDTO> records = page.getRecords().stream().map(OperationLogDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }

    public void deleteOperationLog(BulkOperationCommand<Long> deleteCommand) {
        operationLogService.removeBatchByIds(deleteCommand.getIds());
    }

}
