package com.apelet.domain.system.menu.query;

import cn.hutool.core.util.StrUtil;
import com.apelet.common.core.page.AbstractQuery;
import com.apelet.domain.system.menu.db.SysMenuEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuQuery extends AbstractQuery<SysMenuEntity> {
      // 直接交给前端筛选
    private String menuName;
    private Integer status;

    @Override
    public QueryWrapper<SysMenuEntity> addQueryCondition() {
        QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(menuName), "menu_name", menuName)
            .eq(status != null, "status", status);
        this.orderColumn = "parent_id";
        this.orderDirection = "descending";
        return queryWrapper;
    }
}
