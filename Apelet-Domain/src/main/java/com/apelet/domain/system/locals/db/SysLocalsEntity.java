package com.apelet.domain.system.locals.db;

import com.apelet.common.core.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_locals")
@ApiModel(value = "SysLocalsEntity对象", description = "i18n 表")
public class SysLocalsEntity extends Model<SysLocalsEntity> {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("i18n Id")
    @TableId(value = "locals_Id", type = IdType.AUTO)
    private Long localsId;

    @ApiModelProperty("菜单Id")
    @TableField("menu_id")
    private Long menuId;

    @ApiModelProperty("i18n 字段值")
    @TableField("locals_label")
    private String localsLabel;

    @ApiModelProperty("i18n 中文值")
    @TableField("locals_zh_value")
    private String localsZhValue;

    @ApiModelProperty("i18n 英文值")
    @TableField("locals_en_value")
    private String localsEnValue;

}
