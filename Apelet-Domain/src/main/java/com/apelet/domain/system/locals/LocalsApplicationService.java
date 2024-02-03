package com.apelet.domain.system.locals;

import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.apelet.domain.system.locals.db.SysLocalsService;
import com.apelet.domain.system.locals.dto.LocalsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * i18n 应用服务
 * @author xiaoyuan-zs
 */
@Service
@RequiredArgsConstructor
public class LocalsApplicationService {

    private final SysLocalsService sysLocalsService;

    /**
     * 查询i18n信息
     * @return
     */
    public List<LocalsDTO> getInternational() {
        List<SysLocalsEntity> localsEntities = sysLocalsService.list();
        return localsEntities.stream().map(LocalsDTO::new)
                .collect(Collectors.toList());
    }
}
