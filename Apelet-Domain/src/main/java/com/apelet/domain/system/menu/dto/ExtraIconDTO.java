package com.apelet.domain.system.menu.dto;

import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
public class ExtraIconDTO {

    // 是否是svg
    private boolean svg;
    // iconfont名称，目前只支持iconfont，后续拓展
    private String name;

}
