package com.apelet.domain.system.role.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author xiaoyuan-zs
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateRoleCommand extends AddRoleCommand {

    @NotNull
    @PositiveOrZero
    private Long roleId;

}
