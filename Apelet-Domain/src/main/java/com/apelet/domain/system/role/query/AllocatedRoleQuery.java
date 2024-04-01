package com.apelet.domain.system.role.query;

import cn.hutool.core.util.StrUtil;
import com.apelet.common.core.page.AbstractPageQuery;
import com.apelet.domain.system.user.db.SysUserEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllocatedRoleQuery extends AbstractPageQuery<SysUserEntity> {

    private Long roleId;
    private String username;
    private String phoneNumber;

    @Override
    public QueryWrapper<SysUserEntity> addQueryCondition() {
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("r.role_id", roleId)
            .like(StrUtil.isNotEmpty(username), "u.username", username)
            .like(StrUtil.isNotEmpty(phoneNumber), "u.phone_number", phoneNumber);

        return queryWrapper;
    }

}
