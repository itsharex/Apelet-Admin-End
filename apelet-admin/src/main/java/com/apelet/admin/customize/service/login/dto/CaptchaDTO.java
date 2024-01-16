package com.apelet.admin.customize.service.login.dto;

import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
public class CaptchaDTO {

    private Boolean isCaptchaOn;
    private String captchaCategory;
    private String captchaCodeKey;
    private String captchaCodeImg;

}
