package com.guy.system.login.web;

import com.google.common.collect.Maps;
import com.guy.common.utils.Global;
import com.guy.common.utils.StringUtils;
import com.guy.common.web.BaseController;
import com.guy.system.security.FormAuthenticationFilter;
import com.guy.util.CacheUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录controller
 * @author blank
 * @date 2015年1月14日
 */
@Controller
@RequestMapping(value = "{adminPath}")
public class LoginController extends BaseController{

	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}

	/**
	 * 默认页面
	 * @return
	 */
	@RequestMapping(value="login",method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
        request.getSession().setAttribute("projectName", Global.getConfig("projectName"));
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			return "system/index";
		}else{
			return "system/login";
		}
	}

	/**
	 * 登录失败
	 * @param userName
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);

		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message",message);
		map.put("success",false);
		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			map.put("isValidateCodeLogin", isValidateCodeLogin(userName, true, false));
//			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(userName, true, false));
		}

//
//		// 验证失败清空验证码
//		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
//		if (mobile){
//			return renderString(response, model);
//		}
		return map;
	}

	/**
	 * 登出
	 * @param model
	 * @return
	 */
	@RequestMapping(value="logout")
	public String logout(Model model) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "system/login";
	}

}
