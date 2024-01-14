package com.apelet.admin.customize.config;

import com.apelet.common.exception.ApiException;
import com.apelet.common.exception.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;


/**
 * @see PasswordEncoder 需要SpringSecurity配置类传递passwordEncoder， 否则PasswordEncoder为 null
 * @see userDetailsService 需要SpringSecurity配置类传递userDetailsService， 否则UserDetailsService为 null
 */
@Slf4j
public class UserAuthenticationProvider extends DaoAuthenticationProvider {

    @Resource
    private PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        super();
        setUserDetailsService(userDetailsService);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Spring Security默认的密码比对主要是依靠DaoAuthenticationProvider下的additionalAuthenticationChecks方法来完成的，
     * 我们只需要将additionalAuthenticationChecks方法进行重写，就可以自定义密码比对业务， 详情查看DaoAuthenticationProvider源码
     * @param userDetails
     * @param authentication
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 前端传递的密码
        if (authentication.getCredentials() == null) {
            log.debug("Authentication failed: no credentials provided");
            throw new ApiException(ErrorCode.Business.USER_NON_EXIST);
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            log.debug("Authentication failed: password does not match stored value");
            throw new ApiException(ErrorCode.Business.LOGIN_PRESENTED_NO_MATCH_PASSWORD);
        }

    }
}
