package com.apelet.domain.system.user.dto;

import com.apelet.domain.system.post.dto.PostDTO;
import com.apelet.domain.system.role.dto.RoleDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author xiaoyuan-zs
 */
@Data
public class UserDetailDTO {

    private UserDTO user;

    /**
     * 返回所有role
     */
    private List<RoleDTO> roleOptions;

    /**
     * 返回所有posts
     */
    private List<PostDTO> postOptions;

    private Long postId;

    private Long roleId;

    private Set<String> permissions;

}
