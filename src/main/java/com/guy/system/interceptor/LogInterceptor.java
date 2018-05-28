package com.guy.system.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.guy.common.mapper.JsonMapper;
import com.guy.common.utils.DateUtils;
import com.guy.system.log.entity.Log;
import com.guy.system.log.service.LogService;
import com.guy.system.user.service.UserService;
import com.guy.util.IPUtil;
import com.guy.util.UserUtil;

/**
 * 日志拦截器
 * @author blank
 * @date 2015年1月14日
 */
public class LogInterceptor implements HandlerInterceptor {

	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	private Long beginTime;// 1、开始时间
	private Long endTime;// 2、结束时间

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		beginTime = System.currentTimeMillis();//计时
		return true;
	}

	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		endTime = System.currentTimeMillis();
		String requestRri = request.getRequestURI();
		String uriPrefix = request.getContextPath();
		String operationCode=StringUtils.substringAfter(requestRri,uriPrefix);	//操作编码
		
		String requestParam=(new JsonMapper()).toJson(request.getParameterMap());	//请求参数
		
		//如果是GET请求，请求编码包含create，update(添加修改页)不记录日志
		if(request.getMethod().equals("GET")){
			if(operationCode.contains("create")||operationCode.contains("update")){
				return;
			}
		}
		Long executeTime=endTime-beginTime;
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		String os=userAgent.getOperatingSystem().getName();	//获取客户端操作系统
		String browser=userAgent.getBrowser().getName();	//获取客户端浏览器
		
		Log log=new Log();
		log.setOs(os);
		log.setBrowser(browser);
		log.setIp(IPUtil.getIpAddress(request));
		log.setOperationCode(operationCode);
		log.setExecuteTime(Integer.valueOf(executeTime.toString()));
		log.setCreater(UserUtil.getCurrentUser().getName());
		log.setCreateDate(DateUtils.getSysTimestamp());
		//log.setDescription(LogCodeUtil.matchingOperationCode(operationCode));
		log.setRequestParam(requestParam);
		
		//放到一公共变量里，定时提交
		logService.save(log);
	}

}
