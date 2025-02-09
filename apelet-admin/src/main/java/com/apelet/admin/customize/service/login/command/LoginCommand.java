package com.apelet.admin.customize.service.login.command;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author xiaoyuan-zs
 */
@Data
public class LoginCommand {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 图形验证码
     */
    private String verifyCode;

    /**
     * 唯一标识
     */
    private String captchaCodeKey;

    /**
     * 滑动、点击验证码二次校验token
     */
    private String captchaVerification;

}
