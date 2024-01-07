package com.apelet.admin.controller.common;

import cn.hutool.core.util.StrUtil;
import com.apelet.admin.customize.service.login.LoginService;
import com.apelet.admin.customize.service.login.command.LoginCommand;
import com.apelet.admin.customize.service.login.dto.CaptchaDTO;
import com.apelet.admin.customize.service.login.dto.ConfigDTO;
import com.apelet.common.config.ApeletConfig;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode.Business;
import com.apelet.core.annotations.ratelimit.RateLimit;
import com.apelet.core.annotations.ratelimit.RateLimit.CacheType;
import com.apelet.core.annotations.ratelimit.RateLimit.LimitType;
import com.apelet.core.annotations.ratelimit.RateLimitKey;
import com.apelet.core.user.AuthenticationUtils;
import com.apelet.core.user.web.SystemLoginUser;
import com.apelet.domain.common.dto.CurrentLoginUserDTO;
import com.apelet.domain.common.dto.TokenDTO;
import com.apelet.domain.system.menu.MenuApplicationService;
import com.apelet.domain.system.menu.dto.RouterDTO;
import com.apelet.domain.system.user.UserApplicationService;
import com.apelet.domain.system.user.command.AddUserCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页
 *
 * @author xiaoyuan-zs
 */
@Api(tags = "登录")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final MenuApplicationService menuApplicationService;

    private final UserApplicationService userApplicationService;

    private final ApeletConfig apeletConfig;

    /**
     * 访问首页，提示语
     */
    @ApiOperation(value = "首页")
    @GetMapping("/")
    @RateLimit(key = RateLimitKey.TEST_KEY, time = 10, maxCount = 5, cacheType = CacheType.Map,
            limitType = LimitType.GLOBAL)
    public String index() {
        return StrUtil.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。",
                apeletConfig.getName(), apeletConfig.getVersion());
    }


    /**
     * 获取系统的内置配置
     *
     * @return 配置信息
     */
    @GetMapping("/getConfig")
    @ApiOperation(value = "首页")
    public ResponseDTO<ConfigDTO> getConfig() {
        ConfigDTO configDTO = loginService.getConfig();
        return ResponseDTO.ok(configDTO);
    }

    /**
     * 生成验证码
     */
    @ApiOperation( "验证码")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = CacheType.REDIS,
            limitType = LimitType.IP)
    @GetMapping("/captchaImage")
    public ResponseDTO<CaptchaDTO> getCaptchaImg() {
        CaptchaDTO captchaImg = loginService.generateCaptchaImg();
        return ResponseDTO.ok(captchaImg);
    }

    /**
     * 登录方法
     *
     * @param loginCommand 登录信息
     * @return 结果
     */
    @ApiOperation( "登录")
    @PostMapping("/login")
    public ResponseDTO<TokenDTO> login(@RequestBody LoginCommand loginCommand) {
        // 生成令牌
        String token = loginService.login(loginCommand);
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        CurrentLoginUserDTO currentUserDTO = userApplicationService.getLoginUserInfo(loginUser);

        return ResponseDTO.ok(new TokenDTO(token, currentUserDTO));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation( "获取当前登录用户信息")
    @GetMapping("/getLoginUserInfo")
    public ResponseDTO<CurrentLoginUserDTO> getLoginUserInfo() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();

        CurrentLoginUserDTO currentUserDTO = userApplicationService.getLoginUserInfo(loginUser);

        return ResponseDTO.ok(currentUserDTO);
    }

    /**
     * 获取路由信息
     * TODO 如果要在前端开启路由缓存的话 需要在ServerConfig.json 中  设置CachingAsyncRoutes=true  避免一直重复请求路由接口
     * @return 路由信息
     */
    @ApiOperation( "获取用户对应的菜单路由")
    @GetMapping("/getRouters")
    public ResponseDTO<List<RouterDTO>> getRouters() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        List<RouterDTO> routerTree = menuApplicationService.getRouterTree(loginUser);
        return ResponseDTO.ok(routerTree);
    }


    @ApiOperation( "注册接口")
    @PostMapping("/register")
    public ResponseDTO<Void> register(@RequestBody AddUserCommand command) {
        return ResponseDTO.fail(new ApiException(Business.COMMON_UNSUPPORTED_OPERATION));
    }

}
