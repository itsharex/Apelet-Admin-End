package com.apelet.domain.system.user.dto;

import com.apelet.domain.system.post.db.SysPostEntity;
import com.apelet.domain.system.role.db.SysRoleEntity;
import com.apelet.domain.system.user.db.SysUserEntity;
import lombok.Data;

/**
 * @author xiaoyuan-zs
 */
@Data
public class UserProfileDTO {

    public UserProfileDTO(SysUserEntity userEntity, SysPostEntity postEntity, SysRoleEntity roleEntity) {
        if (userEntity != null) {
            this.user = new UserDTO(userEntity);
        }

        if (postEntity != null) {
            this.postName = postEntity.getPostName();
        }

        if (roleEntity != null) {
            this.roleName = roleEntity.getRoleName();
        }
    }

    private UserDTO user;
    private String roleName;
    private String postName;

}
