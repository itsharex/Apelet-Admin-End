package com.apelet.domain.system.role.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author xiaoyuan-zs
 */
@Data
public class UpdateDataScopeCommand {

    @NotNull
    @Positive
    private Long roleId;

    @NotNull
    @NotEmpty
    private List<Long> deptIds;

    private Integer dataScope;


}
