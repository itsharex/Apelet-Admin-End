package com.apelet.admin.controller.common;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.apelet.admin.customize.service.login.LoginService;
import com.apelet.admin.customize.service.login.command.LoginCommand;
import com.apelet.admin.customize.service.login.dto.CaptchaDTO;
import com.apelet.admin.customize.service.login.dto.ConfigDTO;
import com.apelet.common.config.ApeletAdminConfig;
import com.apelet.common.core.dto.ResponseDTO;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode.Business;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.domain.common.dto.CurrentLoginUserDTO;
import com.apelet.domain.common.dto.TokenDTO;
import com.apelet.domain.system.locals.LocalsApplicationService;
import com.apelet.domain.system.locals.dto.LocalsDTO;
import com.apelet.domain.system.menu.MenuApplicationService;
import com.apelet.domain.system.menu.dto.RouterDTO;
import com.apelet.domain.system.user.UserApplicationService;
import com.apelet.domain.system.user.command.AddUserCommand;
import com.apelet.framework.annotations.ratelimit.RateLimit;
import com.apelet.framework.annotations.ratelimit.RateLimit.CacheType;
import com.apelet.framework.annotations.ratelimit.RateLimit.LimitType;
import com.apelet.framework.annotations.ratelimit.RateLimitKey;
import com.apelet.framework.security.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "登录API", description = "登录相关接口")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final MenuApplicationService menuApplicationService;

    private final UserApplicationService userApplicationService;

    private final LocalsApplicationService localsApplicationService;

    private final ApeletAdminConfig apeletAdminConfig;

    private final CaptchaService captchaService;

    /**
     * 访问首页，提示语
     */
    @Operation(summary = "首页")
    @GetMapping("/")
    @RateLimit(key = RateLimitKey.TEST_KEY, time = 10, maxCount = 5, cacheType = CacheType.Map,
        limitType = LimitType.GLOBAL)
    public String index() {
        return StrUtil.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", apeletAdminConfig.getName(), apeletAdminConfig.getVersion());
    }


    /**
     * 获取系统的内置配置
     *
     * @return 配置信息
     */
    @GetMapping("/getConfig")
    public ResponseDTO<ConfigDTO> getConfig() {
        ConfigDTO configDTO = loginService.getConfig();
        return ResponseDTO.ok(configDTO);
    }

    /**
     *  获取图形验证码
     */
    @Operation(summary = "验证码")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = CacheType.REDIS,
        limitType = LimitType.IP)
    @GetMapping("/captchaImage")
    public ResponseDTO<CaptchaDTO> getCaptchaImg() {
        CaptchaDTO captchaImg = loginService.generateCaptchaImg();
        return ResponseDTO.ok(captchaImg);
    }

    /**
     * 查看验证码是否开启以及获取验证码类别
     * @return 验证码信息
     */
    @GetMapping("/reCaptcha/type")
    public ResponseDTO<CaptchaDTO> getCaptchaType() {
        CaptchaDTO captchaType = loginService.getCaptchaType();
        return ResponseDTO.ok(captchaType);
    }

    /**
     * 生成滑块、点选验证码
     * @param captchaVO
     * @return
     */
    @Operation(summary ="生成滑块、点选验证码")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = CacheType.REDIS,
            limitType = LimitType.IP)
    @PostMapping("/reCaptcha/get")
    public ResponseDTO<CaptchaVO> captchaGet(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.get(captchaVO);
        return ResponseDTO.ok((CaptchaVO)responseModel.getRepData());
    }

    /**
     * 滑块、点选验证码验证
     * @param captchaVO
     * @return
     */
    @Operation(summary ="滑块、点选验证码验证")
    @RateLimit(key = RateLimitKey.LOGIN_CAPTCHA_KEY, time = 10, maxCount = 10, cacheType = CacheType.REDIS,
            limitType = LimitType.IP)
    @PostMapping("/reCaptcha/check")
    public ResponseDTO<CaptchaVO> captchaCheck(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.check(captchaVO);
        return ResponseDTO.ok((CaptchaVO)responseModel.getRepData());
    }


    /**
     * 登录方法
     *
     * @param loginCommand 登录信息
     * @return 结果
     */
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseDTO<TokenDTO> login(@RequestBody LoginCommand loginCommand) {

        // 生成令牌
        String token = loginService.login(loginCommand);
        return ResponseDTO.ok(new TokenDTO(token));
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
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
    @Operation(summary = "获取用户对应的菜单路由", description = "用于动态生成路由")
    @GetMapping("/getRouters")
    public ResponseDTO<List<RouterDTO>> getRouters() {
        SystemLoginUser loginUser = AuthenticationUtils.getSystemLoginUser();
        List<RouterDTO> routerTree = menuApplicationService.getRouterTree(loginUser);
        return ResponseDTO.ok(routerTree);
    }


    /**
     * 获取i18n信息
     * @return i18n信息
     */
    @Operation(summary = "获取菜单对应的i18n信息")
    @GetMapping("/getInternational")
    public ResponseDTO<List<LocalsDTO>> getInternational() {
        List<LocalsDTO> locals = localsApplicationService.getInternational();
        return ResponseDTO.ok(locals);
    }


    @Operation(summary = "注册接口", description = "暂未实现")
    @PostMapping("/register")
    public ResponseDTO<Void> register(@RequestBody AddUserCommand command) {
        return ResponseDTO.fail(new ApiException(Business.COMMON_UNSUPPORTED_OPERATION));
    }

}
