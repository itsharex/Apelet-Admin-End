package com.apelet.domain.system.notice;

import com.apelet.common.core.page.PageDTO;
import com.apelet.domain.common.command.BulkOperationCommand;
import com.apelet.domain.system.notice.command.NoticeAddCommand;
import com.apelet.domain.system.notice.command.NoticeUpdateCommand;
import com.apelet.domain.system.notice.db.SysNoticeEntity;
import com.apelet.domain.system.notice.db.SysNoticeService;
import com.apelet.domain.system.notice.dto.NoticeDTO;
import com.apelet.domain.system.notice.model.NoticeModel;
import com.apelet.domain.system.notice.model.NoticeModelFactory;
import com.apelet.domain.system.notice.query.NoticeQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoyuan-zs
 */
@Service
@RequiredArgsConstructor
public class NoticeApplicationService {

    private final SysNoticeService noticeService;

    private final NoticeModelFactory noticeModelFactory;

    public PageDTO<NoticeDTO> getNoticeList(NoticeQuery query) {
        Page<SysNoticeEntity> page = noticeService.getNoticeList(query.toPage(), query.toQueryWrapper());
        List<NoticeDTO> records = page.getRecords().stream().map(NoticeDTO::new).collect(Collectors.toList());
        return new PageDTO<>(records, page.getTotal());
    }


    public NoticeDTO getNoticeInfo(Long id) {
        NoticeModel noticeModel = noticeModelFactory.loadById(id);
        return new NoticeDTO(noticeModel);
    }


    public void addNotice(NoticeAddCommand addCommand) {
        NoticeModel noticeModel = noticeModelFactory.create();
        noticeModel.loadAddCommand(addCommand);

        noticeModel.checkFields();

        noticeModel.insert();
    }


    public void updateNotice(NoticeUpdateCommand updateCommand) {
        NoticeModel noticeModel = noticeModelFactory.loadById(updateCommand.getNoticeId());
        noticeModel.loadUpdateCommand(updateCommand);

        noticeModel.checkFields();

        noticeModel.updateById();
    }

    public void deleteNotice(BulkOperationCommand<Integer> deleteCommand) {
        noticeService.removeBatchByIds(deleteCommand.getIds());
    }




}
