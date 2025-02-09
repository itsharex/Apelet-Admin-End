package com.apelet.domain.system.menu.dto;

import cn.hutool.core.util.StrUtil;
import com.apelet.common.utils.jackson.JacksonUtil;
import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.apelet.domain.system.menu.db.SysMenuEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 动态路由信息
 * 必须加上@JsonInclude(Include.NON_NULL)的注解  否则传null值给Vue动态路由渲染时会出错
 * @author xiaoyuan-zs
 */
@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class RouterDTO {

    public RouterDTO(SysMenuEntity entity, SysLocalsEntity locale) {
        if (entity != null) {
            this.name = entity.getRouterName();
            this.path = entity.getPath();
            this.component = entity.getComponent();
            this.rank = entity.getSort();
            this.menuSort = entity.getMenuSort();
            if (!StrUtil.isEmpty(entity.getRedirect())) {
                this.redirect = entity.getRedirect();
            }
            if (JacksonUtil.isJson(entity.getMetaInfo())) {
                MetaDTO metaDTO = JacksonUtil.from(entity.getMetaInfo(), MetaDTO.class);
                if (!Objects.isNull(locale) && StrUtil.isNotEmpty(locale.getLocalsLabel())) {
                    metaDTO.setTitle(locale.getLocalsLabel());
                } else {
                    metaDTO.setTitle("menus." + entity.getPath());
                }
                metaDTO.setIsLink(entity.getIsLink());
                metaDTO.setIsFrame(entity.getIsFrame());
                this.meta = metaDTO;
            } else {
                this.meta = new MetaDTO();
            }
            // 获取路由时，暂不需要权限
            // this.meta.setAuths(Lists.newArrayList(entity.getPermission()));
        }
    }

    /**
     * 路由名字  根据前端的要求   必须唯一
     * 并按照前端项目的推荐  这个Name最好和组件的Name一样  使用菜单表中的router_name
     * TODO 这里后端需要加校验
     */
    private String name;

    /**
     * 路由路径地址
     */
    private String path;

    /**
     * 路由重定向
     */
    private String redirect;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 菜单类别
     */
    private Integer menuSort;

    /**
     * 一级菜单排序值（排序仅支持一级菜单）
     */
    private Long rank;

    /**
     * 其他元素
     */
    private MetaDTO meta;

    /**
     * 子路由
     */
    private List<RouterDTO> children;
}
