package com.apelet.domain.system.role.command;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoyuan-zs
 */
@Data
@NoArgsConstructor
public class UpdateStatusCommand {

    private Long roleId;

    private Integer status;

}
