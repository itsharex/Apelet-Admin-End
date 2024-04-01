package com.apelet.admin.customize.service.permission.model.checker;

import com.apelet.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import com.apelet.admin.customize.service.permission.model.DataCondition;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.domain.system.dept.db.SysDeptService;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据权限测试接口
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;

    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        return false;
    }

}
