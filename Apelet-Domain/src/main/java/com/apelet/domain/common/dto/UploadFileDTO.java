package com.apelet.domain.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoyuan-zs
 */
@Data
@NoArgsConstructor
public class UploadFileDTO {

    public UploadFileDTO(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private String imgUrl;

}
