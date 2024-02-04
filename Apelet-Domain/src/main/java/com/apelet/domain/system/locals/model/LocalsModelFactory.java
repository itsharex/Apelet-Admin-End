package com.apelet.domain.system.locals.model;

import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.apelet.domain.system.locals.db.SysLocalsService;
import com.apelet.domain.system.menu.db.SysMenuEntity;
import com.apelet.domain.system.menu.model.MenuModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 国际化模型工程
 * @author xiaoyuan-zs
 */
@Component
@RequiredArgsConstructor
public class LocalsModelFactory {

    private final SysLocalsService localsService;

    public LocalsModel loadById(Long localsId) {
        SysLocalsEntity byId = localsService.getById(localsId);
        if (byId == null) {
            throw new ApiException(ErrorCode.Business.COMMON_OBJECT_NOT_FOUND, localsId, "国际化");
        }
        return new LocalsModel(byId, localsService);
    }

    public LocalsModel create() {
        return new LocalsModel(localsService);
    }

}
