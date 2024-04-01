package com.apelet.domain.system.menu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 路由显示信息
 * 必须加上@JsonInclude(Include.NON_NULL)的注解  否则传null值给Vue动态路由渲染时会出错
 * @author xiaoyuan-zs
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class MetaDTO {
    // 菜单名称（兼容国际化、非国际化，如果用国际化的写法就必须在根目录的i18n文件夹下对应添加）
    private String title;
    // 菜单图标
    private String icon;
    // 跳转到子级菜单是否选中父级菜单
    private String activeMenu;
    // 是否显示该菜单
    private Boolean hidden;
    // 是否固定标签页
    private Boolean isFixed;
    // 是否缓存该菜单
    private Boolean isCache;
    // 页面级别权限设置
    private List<String> roles;
    // 按钮级别权限设置
    private List<String> auths;
    // 需要内嵌的iframe链接地址
    private String iframeSrc;
    // 是否外链
    private Boolean isLink;
    /**
     *  菜单排序，值越高排的越后（只针对顶级路由）
     */
    private Integer rank;


    // =========  目前系统仅支持以上这些参数的设置 后续有需要的话开发者可自行设置的这些参数  ===========

    // 是否缓存该路由页面（开启后，会保存该页面的整体状态，刷新后会清空状态）
    private Boolean keepAlive;

}
