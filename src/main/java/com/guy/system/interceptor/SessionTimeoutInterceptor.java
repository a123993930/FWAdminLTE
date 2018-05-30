package com.guy.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guy.common.utils.StringUtils;
import com.guy.system.permission.entity.Permission;
import com.guy.system.permission.service.PermissionService;
import com.guy.util.SpringContextHolder;
import com.guy.util.UserAgentUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.guy.system.user.entity.User;

import java.util.List;

/** 
 * 处理session超时的拦截器 
 */  
public class SessionTimeoutInterceptor  implements HandlerInterceptor{  
      
    public String[] allowUrls;//还没发现可以直接配置不拦截的资源，所以在代码里面来排除  
      
    public void setAllowUrls(String[] allowUrls) {  
        this.allowUrls = allowUrls;  
    }  
  
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,  
            Object arg2) throws Exception {
         String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
//        System.out.println(requestUrl);
        if(null != allowUrls && allowUrls.length>=1)
            for(String url : allowUrls) {
                if(requestUrl.contains(url)) {
                    return true;
                }
            }

        User user = (User) request.getSession().getAttribute("user");
//        if(user != null) {
//            return true;  //返回true，则这个方面调用后会接着调用postHandle(),  afterCompletion()
//        }else{
//        	request.getRequestDispatcher("/WEB-INF/views/system/login.jsp").forward(request, response);
//        	return  true;
//        }


        List<Permission> permissionList = SpringContextHolder.getBean(PermissionService.class).getMenus();
        request.getSession().setAttribute("perMenu",permissionList);
        return true;
    }  
      
    @Override  
    public void afterCompletion(HttpServletRequest arg0,  
            HttpServletResponse arg1, Object arg2, Exception arg3)  
            throws Exception {  
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null){
//            // 如果是手机或平板访问的话，则跳转到手机视图页面。
//            if(UserAgentUtils.isMobileOrTablet(request) && !StringUtils.startsWithIgnoreCase(modelAndView.getViewName(), "redirect:")){
//                modelAndView.setViewName("mobile/" + modelAndView.getViewName());
//            }
//        }
    }
  
}  