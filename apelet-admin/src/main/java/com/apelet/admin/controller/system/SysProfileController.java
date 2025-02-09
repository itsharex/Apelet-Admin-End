package com.apelet.admin.controller.system;

import com.apelet.admin.customize.aop.accessLog.AccessLog;
import com.apelet.common.constant.Constants.UploadSubDir;
import com.apelet.common.core.base.BaseController;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.enums.common.BusinessTypeEnum;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.common.utils.file.FileUploadUtils;
import com.apelet.domain.common.dto.UploadFileDTO;
import com.apelet.domain.system.user.UserApplicationService;
import com.apelet.domain.system.user.command.UpdateProfileCommand;
import com.apelet.domain.system.user.command.UpdateUserAvatarCommand;
import com.apelet.domain.system.user.command.UpdateUserPasswordCommand;
import com.apelet.domain.system.user.dto.UserProfileDTO;
import com.apelet.framework.security.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 个人信息 业务处理
 *
 * @author ruoyi
 */
@Tag(name = "个人信息API", description = "个人信息相关接口")
@RestController
@RequestMapping("/system/user/profile")
@RequiredArgsConstructor
public class SysProfileController extends BaseController {

    private final UserApplicationService userApplicationService;

    /**
     * 个人信息
     */
    @Operation(summary = "获取个人信息")
    @GetMapping
    public ResponseDTO<UserProfileDTO> profile() {
        SystemLoginUser user = AuthenticationUtils.getSystemLoginUser();
        UserProfileDTO userProfile = userApplicationService.getUserProfile(user.getUserId());
        return ResponseDTO.ok(userProfile);
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改个人信息")
    @AccessLog(title = "个人信息", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping
    public ResponseDTO<Void> updateProfile(@RequestBody UpdateProfileCommand command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        command.setUserId(loginUser.getUserId());
        userApplicationService.updateUserProfile(command);
        return ResponseDTO.ok();
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置个人密码")
    @AccessLog(title = "个人信息", businessType = BusinessTypeEnum.MODIFY)
    @PutMapping("/password")
    public ResponseDTO<Void> updatePassword(@RequestBody UpdateUserPasswordCommand command) {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        command.setUserId(loginUser.getUserId());
        userApplicationService.updatePasswordBySelf(loginUser, command);
        return ResponseDTO.ok();
    }

    /**
     * 头像上传
     */
    @Operation(summary = "修改个人头像")
    @AccessLog(title = "用户头像", businessType = BusinessTypeEnum.MODIFY)
    @PostMapping("/avatar")
    public ResponseDTO<UploadFileDTO> avatar(@RequestParam("avatarfile") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException(ErrorCode.Business.USER_UPLOAD_FILE_FAILED);
        }
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        String avatarUrl = FileUploadUtils.upload(UploadSubDir.AVATAR_PATH, file);

        userApplicationService.updateUserAvatar(new UpdateUserAvatarCommand(loginUser.getUserId(), avatarUrl));
        return ResponseDTO.ok(new UploadFileDTO(avatarUrl));
    }
}
