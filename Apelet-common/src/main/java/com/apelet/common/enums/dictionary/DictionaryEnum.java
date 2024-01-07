package com.apelet.common.enums.dictionary;


import com.apelet.common.enums.BasicEnum;

/**
 * 字典类型 接口
 * @author xiaoyuan-zs
 */
public interface DictionaryEnum<T> extends BasicEnum<T> {

    /**
     * 获取css标签
     * @return css标签
     */
    String cssTag();

}
