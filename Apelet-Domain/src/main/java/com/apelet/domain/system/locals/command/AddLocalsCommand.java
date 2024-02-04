package com.apelet.domain.system.locals.command;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaoyuan-zs
 */
@Data
public class AddLocalsCommand {

    private Long localsId;

    private Long menuId;

    @NotBlank(message = "国际化字段不能为空")
    private String localsLabel;

    private String localsZhValue;

    private String localsEnValue;

}
