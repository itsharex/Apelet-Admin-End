package com.apelet.admin.customize.service.permission.model.checker;

import com.apelet.admin.customize.service.permission.model.AbstractDataPermissionChecker;
import com.apelet.admin.customize.service.permission.model.DataCondition;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.domain.system.dept.db.SysDeptService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 数据权限测试接口
 * @author valarchie
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlySelfDataPermissionChecker extends AbstractDataPermissionChecker {

    private SysDeptService deptService;

    @Override
    public boolean check(SystemLoginUser loginUser, DataCondition condition) {
        if (condition == null || loginUser == null) {
            return false;
        }

        if (loginUser.getUserId() == null || condition.getTargetUserId() == null) {
            return false;
        }

        Long currentUserId = loginUser.getUserId();
        Long targetUserId = condition.getTargetUserId();
        return Objects.equals(currentUserId, targetUserId);
    }

}
