package com.apelet.domain.common.dto;

import com.apelet.domain.system.user.dto.UserDTO;
import lombok.Data;

import java.util.Set;

/**
 * @author xiaoyuan-zs
 */
@Data
public class CurrentLoginUserDTO {

    private UserDTO userInfo;
    private String roleKey;
    private Set<String> permissions;

}
