package com.apelet.admin.customize.service.login.dto;

import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class CaptchaDTO {

    private Boolean isCaptchaOn;
    private Boolean isGraphical;
    private String captchaCategory;
    private String captchaCodeKey;
    private String captchaCodeImg;

}
