package com.apelet.domain.system.user.command;

import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
public class UpdateUserPasswordCommand {

    private Long userId;
    private String newPassword;
    private String oldPassword;

}
