package com.apelet.domain.system.menu.query;

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
//    private String menuName;
//    private Boolean isVisible;
//    private Integer status;
    private Boolean isButton;

    @Override
    public QueryWrapper<SysMenuEntity> addQueryCondition() {
        QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(isButton != null, "is_button", isButton);
//            .like(StrUtil.isNotEmpty(menuName), "menu_name", menuName)
//            .eq(isVisible != null, "is_visible", isVisible)
//            .eq(status != null, "status", status);
        this.orderColumn = "parent_id";
        this.orderDirection = "descending";
        return queryWrapper;
    }
}
