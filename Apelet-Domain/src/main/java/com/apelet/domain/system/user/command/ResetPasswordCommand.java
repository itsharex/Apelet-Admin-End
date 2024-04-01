package com.apelet.domain.system.user.command;

import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
public class ResetPasswordCommand {

    private Long userId;
    private String password;

}
