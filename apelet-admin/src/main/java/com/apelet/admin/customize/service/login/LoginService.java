package com.apelet.admin.customize.service.login;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.extra.servlet.ServletUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.apelet.admin.customize.async.AsyncTaskFactory;
import com.apelet.admin.customize.service.login.command.LoginCommand;
import com.apelet.admin.customize.service.login.dto.CaptchaDTO;
import com.apelet.admin.customize.service.login.dto.ConfigDTO;
import com.apelet.common.config.ApeletAdminConfig;
import com.apelet.common.constant.Constants;
import com.apelet.common.enums.common.ConfigKeyEnum;
import com.apelet.common.enums.common.LoginStatusEnum;
import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import com.apelet.common.exception.error.ErrorCode.Business;
import com.apelet.common.user.web.SystemLoginUser;
import com.apelet.common.utils.ServletHolderUtil;
import com.apelet.common.utils.i18n.MessageUtils;
import com.apelet.domain.common.cache.GuavaCacheService;
import com.apelet.domain.common.cache.MapCache;
import com.apelet.domain.common.cache.RedisCacheService;
import com.apelet.domain.system.user.db.SysUserEntity;
import com.apelet.framework.thread.ThreadPoolManager;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;

/**
 * 登录校验方法
 *
 * @author xiaoyuan-zs
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final TokenService tokenService;

    private final RedisCacheService redisCache;

    private final GuavaCacheService guavaCache;

    private final AuthenticationManager authenticationManager;

    private final CaptchaService captchaService;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    /**
     * 登录验证
     *
     * @param loginCommand 登录参数
     * @return 结果
     */
    public String login(LoginCommand loginCommand) {
        // 验证码开关
        if (isCaptchaOn()) {
            // 校验图形验证码
            // validateCaptcha(loginCommand.getUsername(), loginCommand.getVerifyCode(), loginCommand.getCaptchaCodeKey());
            // 校验滑块、点选验证码
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(loginCommand.getCaptchaVerification());
            ResponseModel responseModel = captchaService.verification(captchaVO);
            if (!responseModel.isSuccess()) {
                ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL,
                        responseModel.getRepMsg()));
                throw new ApiException(Business.LOGIN_ERROR, responseModel.getRepMsg());
            }
        }
        // 用户验证
        Authentication authentication;
        // 对前端加密的密码进行解密
        String decryptPassword = decryptPassword(loginCommand.getPassword());
        try {
            // 该方法会去调用UserDetailsServiceImpl#loadUserByUsername  校验用户名和密码  认证鉴权
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginCommand.getUsername(), decryptPassword));
        } catch (BadCredentialsException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL,
                MessageUtils.message("Business.LOGIN_WRONG_USER_PASSWORD")));
            throw new ApiException(e, Business.LOGIN_WRONG_USER_PASSWORD);
        } catch (AuthenticationException e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, Business.LOGIN_ERROR, e.getMessage());
        } catch (Exception e) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginCommand.getUsername(), LoginStatusEnum.LOGIN_FAIL, e.getMessage()));
            throw new ApiException(e, Business.LOGIN_ERROR, e.getMessage());
        }
        // 把当前登录用户 放入上下文中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 这里获取的loginUser是UserDetailsServiceImpl#loadUserByUsername方法返回的LoginUser
        SystemLoginUser loginUser = (SystemLoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser);
        // 生成token
        return tokenService.createTokenAndPutUserInCache(loginUser);
    }

    /**
     * 获取参数配置 data
     *
     * @return {@link ConfigDTO}
     */
    public ConfigDTO getConfig() {
        ConfigDTO configDTO = new ConfigDTO();

        boolean isCaptchaOn = isCaptchaOn();
        configDTO.setIsCaptchaOn(isCaptchaOn);
        configDTO.setDictionary(MapCache.dictionaryCache());
        return configDTO;
    }

    /**
     * 查看验证码是否开启以及获取验证码类别
     * @return {@link CaptchaDTO}
     */
    public CaptchaDTO getCaptchaType() {
        CaptchaDTO captchaDTO = new CaptchaDTO();
        boolean isCaptchaOn = isCaptchaOn();
        captchaDTO.setIsCaptchaOn(isCaptchaOn);
        if (isCaptchaOn) captchaDTO.setCaptchaCategory(ApeletAdminConfig.getCaptchaCategory());
        return captchaDTO;
    }

    /**
     * 获取图形验证码 data
     *
     * @return 验证码
     */
    public CaptchaDTO generateCaptchaImg() {
        CaptchaDTO captchaDTO = new CaptchaDTO();
        // 查看是否开启验证码
        boolean isCaptchaOn = isCaptchaOn();
        captchaDTO.setIsCaptchaOn(isCaptchaOn);
        if (isCaptchaOn) {
            String expression;
            String answer = null;
            BufferedImage image = null;

            // 生成验证码 获取验证码类型 math 数组计算 char 字符验证
            String captchaType = ApeletAdminConfig.getCaptchaType();
            if (Constants.Captcha.MATH_TYPE.equals(captchaType)) {
                String capText = captchaProducerMath.createText();
                String[] expressionAndAnswer = capText.split("@");
                expression = expressionAndAnswer[0];
                answer = expressionAndAnswer[1];
                image = captchaProducerMath.createImage(expression);
            }

            if (Constants.Captcha.CHAR_TYPE.equals(captchaType)) {
                expression = answer = captchaProducer.createText();
                image = captchaProducer.createImage(expression);
            }
            if (image == null) {
                throw new ApiException(ErrorCode.Internal.LOGIN_CAPTCHA_GENERATE_FAIL);
            }
            // 保存验证码信息
            String imgKey = IdUtil.simpleUUID();
            redisCache.captchaCache.set(imgKey, answer);
            // 转换流信息写出
            FastByteArrayOutputStream os = new FastByteArrayOutputStream();
            ImgUtil.writeJpg(image, os);
            captchaDTO.setCaptchaCodeKey(imgKey);
            captchaDTO.setCaptchaCodeImg(Base64.encode(os.toByteArray()));
        }
        return captchaDTO;
    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param captchaCode 验证码
     * @param captchaCodeKey 验证码对应的缓存key
     */
    public void validateCaptcha(String username, String captchaCode, String captchaCodeKey) {
        // 查询redis中的验证码
        String captcha = redisCache.captchaCache.getObjectById(captchaCodeKey);
        redisCache.captchaCache.delete(captchaCodeKey);
        if (captcha == null) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL,
                ErrorCode.Business.LOGIN_CAPTCHA_CODE_EXPIRE.message()));
            throw new ApiException(ErrorCode.Business.LOGIN_CAPTCHA_CODE_EXPIRE);
        }
        if (!captchaCode.equalsIgnoreCase(captcha)) {
            ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(username, LoginStatusEnum.LOGIN_FAIL,
                ErrorCode.Business.LOGIN_CAPTCHA_CODE_WRONG.message()));
            throw new ApiException(ErrorCode.Business.LOGIN_CAPTCHA_CODE_WRONG);
        }
    }

    /**
     * 记录登录信息
     * @param loginUser 登录用户
     */
    public void recordLoginInfo(SystemLoginUser loginUser) {
        ThreadPoolManager.execute(AsyncTaskFactory.loginInfoTask(loginUser.getUsername(), LoginStatusEnum.LOGIN_SUCCESS,
            LoginStatusEnum.LOGIN_SUCCESS.description()));

        SysUserEntity entity = redisCache.userCache.getObjectById(loginUser.getUserId());

        entity.setLoginIp(ServletUtil.getClientIP(ServletHolderUtil.getRequest()));
        entity.setLoginDate(DateUtil.date());
        entity.updateById();
    }

    /**
     * 对前端传递的加密密码进行解密
     * @param originalPassword 前端传递的加密密码
     * @return
     */
    public String decryptPassword(String originalPassword) {
        byte[] decryptBytes = SecureUtil.rsa(ApeletAdminConfig.getRsaPrivateKey(), null)
            .decrypt(Base64.decode(originalPassword), KeyType.PrivateKey);

        return StrUtil.str(decryptBytes, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 查看参数配置中是否开启验证码功能
     * @return
     */
    private boolean isCaptchaOn() {

        return Convert.toBool(guavaCache.configCache.get(ConfigKeyEnum.CAPTCHA.getValue()));
    }

}
