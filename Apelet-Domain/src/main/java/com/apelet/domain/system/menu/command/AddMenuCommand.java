package com.apelet.domain.system.menu.command;

import com.apelet.domain.system.menu.dto.MetaDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author xiaoyuan-zs
 */
@Data
public class AddMenuCommand {

    private Long parentId;
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50个字符")
    private String menuName;
    /**
     * 路由名称 必须唯一
     */
    private String routerName;

    /**
     * 国际化字段
     */
    private String localsLabel;

    /**
     * 国际化英文值
     */
    private String localsEnValue;

    @Size(max = 200, message = "路由地址不能超过200个字符")
    private String path;

    private Integer status;

    private Integer menuType;

    private Boolean isButton;

    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String permission;

    private MetaDTO meta;

}
