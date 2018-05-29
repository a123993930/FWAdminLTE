/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.guy.system.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guy.common.utils.StringUtils;
import com.guy.util.UsernamePasswordCaptchaToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 表单验证（包含验证码）过滤类
 * @author ThinkGem
 * @version 2013-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "msg";
	private String messageParam = DEFAULT_MESSAGE_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String captchaParam = DEFAULT_CAPTCHA_PARAM;


	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}

	public void setMobileLoginParam(String mobileLoginParam) {
		this.mobileLoginParam = mobileLoginParam;
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String captcha = getCaptcha(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		return new UsernamePasswordCaptchaToken(username,password.toCharArray(), rememberMe, host, captcha);
	}
	public String getMessageParam() {
		return messageParam;
	}
	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
									 AuthenticationException e, ServletRequest request, ServletResponse response) {
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)){
			message = "用户或密码错误, 请重试.";
		}
		else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
			message = StringUtils.replace(e.getMessage(), "msg:", "");
		}
		else{
			message = "系统出现点问题，请稍后再试！";
			e.printStackTrace(); // 输出到控制台
		}
		request.setAttribute(getFailureKeyAttribute(), className);
		request.setAttribute(getMessageParam(), message);
		return true;
	}

	/**
	 * 当登录成功
	 * @param token
	 * @param subject
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		//将user对象放入session，这里你可能用不到，可以删除
		//----------以上代码你可以删除-------------------

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			issueSuccessRedirect(request, response);
		} else {
			httpServletResponse.setCharacterEncoding("UTF-8");
			PrintWriter out = httpServletResponse.getWriter();
			out.println("{\"success\":true,\"message\":\"登入成功\"}");
			out.flush();
			out.close();
		}
		return false;
	}

}