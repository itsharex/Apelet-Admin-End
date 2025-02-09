package com.apelet.domain.system.menu.dto;

import cn.hutool.core.util.StrUtil;
import com.apelet.common.enums.BasicEnumUtil;
import com.apelet.common.enums.common.MenuTypeEnum;
import com.apelet.common.enums.common.StatusEnum;
import com.apelet.common.utils.jackson.JacksonUtil;
import com.apelet.domain.system.menu.db.SysMenuEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author xiaoyuan-zs
 */
@Data
@NoArgsConstructor
public class MenuDTO {

    public MenuDTO(SysMenuEntity entity) {
        if (entity != null) {
            this.id = entity.getMenuId();
            this.parentId = entity.getParentId();
            this.menuName = entity.getMenuName();
            this.routerName = entity.getRouterName();
            this.path = entity.getPath();
            this.status = entity.getStatus();
            this.isButton = entity.getIsButton();
            this.isLink = entity.getIsLink();
            this.isFrame = entity.getIsFrame();
            this.menuSort = entity.getMenuSort();
            this.rank = entity.getSort();
            this.permission = entity.getPermission();
            this.statusStr = BasicEnumUtil.getDescriptionByValue(StatusEnum.class, entity.getStatus());

            if (!entity.getIsButton()) {
                this.menuType = entity.getMenuType();
                this.menuTypeStr = BasicEnumUtil.getDescriptionByValue(MenuTypeEnum.class, entity.getMenuType());
            } else {
                this.menuType = 0;
            }

            if (StrUtil.isNotEmpty(entity.getMetaInfo()) && JacksonUtil.isJson(entity.getMetaInfo())) {
                MetaDTO meta = JacksonUtil.from(entity.getMetaInfo(), MetaDTO.class);
                this.icon = meta.getIcon();
            }
            this.createTime = entity.getCreateTime();
        }
    }

    // 设置成id和parentId 便于前端处理树级结构
    private Long id;

    private Long parentId;

    private String menuName;

    private String routerName;

    private String path;

    private Long rank;

    private Integer menuType;

    private String menuTypeStr;

    private Integer menuSort;

    private String permission;

    private Boolean isButton;

    private Boolean isLink;

    private Boolean isFrame;

    private Integer status;

    private String statusStr;

    private Date createTime;

    private String icon;

}
