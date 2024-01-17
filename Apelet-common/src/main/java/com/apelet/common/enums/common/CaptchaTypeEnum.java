package com.apelet.common.enums.common;

import com.apelet.common.enums.BasicEnum;

public enum CaptchaTypeEnum implements BasicEnum<String> {

    /**
     * 菜单类型
     */
    CAPTCHA_TYPE_GRAPHICAL("graphical", "图形验证码"),
    CAPTCHA_TYPE_BLOCK_PUZZLE("blockPuzzle", "滑块验证码"),
    CAPTCHA_TYPE_CLICK_WORD("clickWord", "点选验证码");

    private final String value;
    private final String description;

    CaptchaTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }


}
