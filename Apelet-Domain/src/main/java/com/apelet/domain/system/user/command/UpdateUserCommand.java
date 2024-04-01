package com.apelet.domain.system.user.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateUserCommand extends AddUserCommand {

    private Long userId;

}
