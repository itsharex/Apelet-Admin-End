package com.apelet.common.enums.dictionary;

import com.apelet.common.enums.DictionaryEnum;
import lombok.Data;

/**
 * 字典模型类
 * @author xiaoyuan-zs
 */
@Data
public class DictionaryData {

    private String label;
    private Integer value;
    private String cssTag;

    @SuppressWarnings("rawtypes")
    public DictionaryData(DictionaryEnum enumType) {
        if (enumType != null) {
            this.label = enumType.description();
            this.value = (Integer) enumType.getValue();
            this.cssTag = enumType.cssTag();
        }
    }

}
