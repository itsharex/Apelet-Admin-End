package com.apelet.domain.common.dto;

import cn.hutool.core.lang.tree.Tree;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaoyuan-zs
 */
@Data
@NoArgsConstructor
public class TreeSelectedDTO {

    private List<Long> checkedKeys;
    private List<Tree<Long>> menus;
    private List<Tree<Long>> depts;

}
