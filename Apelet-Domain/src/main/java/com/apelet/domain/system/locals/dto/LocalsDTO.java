package com.apelet.domain.system.locals.dto;

import com.apelet.domain.system.locals.db.SysLocalsEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * i18n
 * 必须加上@JsonInclude(Include.NON_NULL)的注解  否则传null值给Vue动态路由渲染时会出错
 * @author xiaoyuan-zs
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class LocalsDTO {

    public LocalsDTO(SysLocalsEntity entity) {
        this.localsId = entity.getLocalsId();
        this.menuId = entity.getMenuId();
        this.localsLabel = entity.getLocalsLabel();
        this.localsZhValue = entity.getLocalsZhValue();
        this.localsEnValue = entity.getLocalsEnValue();
    }

    @ApiModelProperty("i18n Id")
    private Long localsId;

    @ApiModelProperty("菜单Id")
    private Long menuId;

    @ApiModelProperty("i18n 字段值")
    private String localsLabel;

    @ApiModelProperty("i18n 中文值")
    private String localsZhValue;

    @ApiModelProperty("i18n 英文值")
    private String localsEnValue;

}
