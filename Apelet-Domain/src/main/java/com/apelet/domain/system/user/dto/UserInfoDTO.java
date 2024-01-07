package com.apelet.domain.system.user.dto;

import com.apelet.domain.system.role.dto.RoleDTO;
import lombok.Data;

/**
 * @author valarchie
 */
@Data
public class UserInfoDTO {

    private UserDTO user;
    private RoleDTO role;

}
