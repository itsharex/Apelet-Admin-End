package com.apelet.common.core.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * 分页模型类
 * @author xiaoyuan-zs
 */
@Data
public class PageDTO<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    public PageDTO(List<T> list) {
        this.rows = list;
        this.total = (long) list.size();
    }

    public PageDTO(Page<T> page) {
        this.rows = page.getRecords();
        this.total = page.getTotal();
    }

    public PageDTO(List<T> list, Long count) {
        this.rows = list;
        this.total = count;
    }

}
