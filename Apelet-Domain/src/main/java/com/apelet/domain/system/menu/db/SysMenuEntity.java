package com.apelet.domain.system.menu.db;

import com.apelet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author xiaoyuan-zs
 * @since 2023-07-21
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "SysMenuEntity对象", description = "菜单权限表")
public class SysMenuEntity extends BaseEntity<SysMenuEntity> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单ID")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @ApiModelProperty("菜单名称")
    @TableField("menu_name")
    private String menuName;

    @ApiModelProperty("菜单的类型(1为普通菜单2为目录3为iFrame4为外部网站)")
    @TableField("menu_type")
    private Integer menuType;

    @ApiModelProperty("路由名称（需保持和前端对应的vue文件中的name保持一致defineOptions方法中设置的name）")
    @TableField("router_name")
    private String routerName;

    @ApiModelProperty("父菜单ID")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("路由path")
    @TableField("path")
    private String path;

    @ApiModelProperty("组件路径（对应前端项目view文件夹中的路径）")
    @TableField("component")
    private String component;

    @ApiModelProperty("是否按钮")
    @TableField("is_button")
    private Boolean isButton;

    @ApiModelProperty("是否外链")
    @TableField("is_link")
    private Boolean isLink;

    @ApiModelProperty("是否内嵌frame")
    @TableField("is_frame")
    private Boolean isFrame;

    @ApiModelProperty("权限标识")
    @TableField("permission")
    private String permission;

    @ApiModelProperty("路由重定向")
    @TableField("redirect")
    private String redirect;

    @ApiModelProperty("路由元信息（前端根据这个信息进行逻辑处理）")
    @TableField("meta_info")
    private String metaInfo;

    @ApiModelProperty("菜单类别")
    @TableField("`menu_sort`")
    private Integer menuSort;

    @ApiModelProperty("菜单排序")
    @TableField("`sort`")
    private Long sort;

    @ApiModelProperty("菜单状态（1启用 0停用）")
    @TableField("`status`")
    private Integer status;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;


    @Override
    public Serializable pkVal() {
        return this.menuId;
    }

}
