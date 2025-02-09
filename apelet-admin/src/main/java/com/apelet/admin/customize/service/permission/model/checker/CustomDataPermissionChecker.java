package com.apelet.admin.customize.service.permission.model.checker;

import cn.hutool.core.collection.CollUtil;
import com.apelet.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import com.apelet.admin.customize.service.permission.model.DataCondition;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.domain.system.dept.db.SysDeptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 数据权限测试接口
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;


    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        if (condition == null || loginUser == null) {
            return false;
        }

        if (loginUser.getRoleInfo() == null) {
            return false;
        }

        Set<Long> deptIdSet = loginUser.getRoleInfo().getDeptIdSet();
        Long targetDeptId = condition.getTargetDeptId();

        return condition.getTargetDeptId() != null && CollUtil.safeContains(deptIdSet, targetDeptId);
    }
}
