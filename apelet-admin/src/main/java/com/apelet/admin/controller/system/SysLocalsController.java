package com.apelet.admin.controller.system;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单信息
 *
 * @author xiaoyuan-zs
 */
@Tag(name = "i18n API", description = "国际化的增删查改")
@RestController
@RequestMapping("/system/locals")
@Validated
@RequiredArgsConstructor
public class SysLocalsController {


}
