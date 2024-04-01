package com.apelet.common.core.page;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageTableDTO<T> {
    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageTableDTO(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public static <T> PageTableDTO<T> build(IPage<T> page) {
        PageTableDTO<T> rspData = new PageTableDTO<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    public static <T> PageTableDTO<T> build(List<T> list) {
        PageTableDTO<T> rspData = new PageTableDTO<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    public static <T> PageTableDTO<T> build() {
        PageTableDTO<T> rspData = new PageTableDTO<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        return rspData;
    }
}
