package com.apelet.domain.system.notice.dto;

import com.apelet.domain.common.cache.CacheCenter;
import com.apelet.domain.system.notice.db.SysNoticeEntity;
import com.apelet.domain.system.user.db.SysUserEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author xiaoyuan-zs
 */
@Data
public class NoticeDTO {

    public NoticeDTO(SysNoticeEntity entity) {
        if (entity != null) {
            this.noticeId = entity.getNoticeId() + "";
            this.noticeTitle = entity.getNoticeTitle();
            this.noticeType = entity.getNoticeType();
            this.noticeContent = entity.getNoticeContent();
            this.status = entity.getStatus();
            this.createTime = entity.getCreateTime();

            SysUserEntity cacheUser = CacheCenter.userCache.getObjectById(entity.getCreatorId());
            if (cacheUser != null) {
                this.creatorName = cacheUser.getUsername();
            }
        }
    }

    private String noticeId;

    private String noticeTitle;

    private Integer noticeType;

    private String noticeContent;

    private Integer status;

    private Date createTime;

    private String creatorName;

}
