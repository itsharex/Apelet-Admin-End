package com.apelet.admin.customize.service.login.dto;

import com.apelet.common.enums.dictionary.DictionaryData;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoyuan-zs
 */
@Data
public class ConfigDTO {

    private Boolean isCaptchaOn;

    private Map<String, List<DictionaryData>> dictionary;

}
