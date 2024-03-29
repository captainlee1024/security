package com.lee.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String LEE_FORM_MOBILE_KEY = "mobile";
//    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private String mobileParameter = LEE_FORM_MOBILE_KEY;
//    private String passwordParameter = "password";
    private boolean postOnly = true;//只处理post请求

    public SmsCodeAuthenticationFilter() {
        // 请求适配器
        // 拦截的请求是什么，登录页表单提交的请求
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }

    //认证流程
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = this.obtainMobile(request);
            if (mobile == null) {
                mobile = "";
            }

//            if (password == null) {
//                password = "";
//            }

            mobile = mobile.trim();//去掉字符串中的空格
            // 实例化Token
            SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);
            this.setDetails(request, authRequest);
            // 用实例化的Token去调用SpringSecurity的AuthenticationManager,
            // 然后由这个AuthenticationManager去调用我们自定义的SmsAuthenticationProvider
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

//    protected String obtainPassword(HttpServletRequest request) {
//        return request.getParameter(this.mobileParameter);
//    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

//    public void setPasswordParameter(String passwordParameter) {
//        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
//        this.mobileParameter = passwordParameter;
//    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return this.mobileParameter;
    }

//    public final String getPasswordParameter() {
//        return this.passwordParameter;
//}

}
