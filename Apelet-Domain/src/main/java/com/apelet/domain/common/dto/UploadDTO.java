package com.apelet.domain.common.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
@Builder
public class UploadDTO {

    private String url;
    private String fileName;
    private String newFileName;
    private String originalFilename;

}
