package com.apelet.domain.system.locals.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.apelet.domain.system.locals.command.AddLocalsCommand;
import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.apelet.domain.system.locals.db.SysLocalsService;
import com.apelet.domain.system.menu.command.AddMenuCommand;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class LocalsModel extends SysLocalsEntity {

    private SysLocalsService localsService;

    public LocalsModel(SysLocalsService localsService) {
        this.localsService = localsService;
    }

    public LocalsModel(SysLocalsEntity localsEntity, SysLocalsService localsService) {
        if(!Objects.isNull(localsEntity)) {
            BeanUtil.copyProperties(localsEntity, this);
        }
        this.localsService = localsService;
    }

    public void loadAddCommand(AddLocalsCommand addLocalsCommand) {
        if (!Objects.isNull(addLocalsCommand)) {
            BeanUtil.copyProperties(addLocalsCommand, this);
        }
    }

    /**
     * 添加菜单国际化
     * @param menuId
     * @param addCommand
     */
    public void loadLocalsByMenuId(Long menuId, AddMenuCommand addCommand) {
        this.setMenuId(menuId);
        // title 则为国际化的label字段
        this.setLocalsLabel(StrUtil.isNotEmpty(addCommand.getLocalsLabel()) ? addCommand.getLocalsLabel() : "menus." + addCommand.getPath());
        this.setLocalsZhValue(addCommand.getMenuName());
        this.setLocalsEnValue(StrUtil.isNotEmpty(addCommand.getLocalsEnValue()) ? addCommand.getLocalsEnValue() : addCommand.getRouterName());
    }

}
