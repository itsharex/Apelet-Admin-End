package com.apelet.domain.system.monitor.dto;

import lombok.Data;

import java.util.List;
import java.util.Properties;


/**
 * @author xiaoyuan-zs
 */
@Data
public class RedisCacheInfoDTO {

    private Properties info;
    private Long dbSize;
    private List<CommandStatusDTO> commandStats;

    @Data
    public static class CommandStatusDTO {
        private String name;
        private String value;
    }

}
