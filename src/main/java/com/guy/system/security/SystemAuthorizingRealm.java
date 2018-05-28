/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.guy.system.security;

import com.guy.common.utils.PropertiesLoader;
import com.guy.common.utils.UserUtils;
import com.guy.system.permission.entity.Permission;
import com.guy.system.permission.service.PermissionService;
import com.guy.system.user.entity.User;
import com.guy.system.user.entity.UserRole;
import com.guy.system.user.service.UserService;
import com.guy.util.CaptchaException;
import com.guy.util.SpringContextHolder;
import com.guy.util.UsernamePasswordCaptchaToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 系统安全认证实现类
 * @author ThinkGem
 * @version 2013-5-29
 */
@Service
@DependsOn({"userDao","permissionDao","rolePermissionDao"})
public class SystemAuthorizingRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;


	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordCaptchaToken token=null;
        User user=null;
        try {
            token = (UsernamePasswordCaptchaToken) authcToken;
            user = userService.getUser(token.getUsername());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (user != null&&doCaptchaValidate(token)) {
            byte[] salt = com.guy.common.utils.security.Encodes.decodeHex(user.getSalt());
//            UserRealm.ShiroUser shiroUser=new UserRealm.ShiroUser(user.getId(), user.getLoginName(), user.getName(),token.getHost());
            //设置用户session
            Session session =SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            //读取websocket配置
            String fileName= "/websocket.properties";
            PropertiesLoader loader= new PropertiesLoader(fileName);
            session.setAttribute("WIMIP",  loader.getProperty("WIMIP"));
            session.setAttribute("WIMPORT",  loader.getProperty("WIMPORT"));
            session.setAttribute("OLIP",  loader.getProperty("OLIP"));
            session.setAttribute("OLPORT",  loader.getProperty("OLPORT"));
            //ckfinder 添加权限
            //start
            Set<UserRole> userRoles=user.getUserRoles();
            for(UserRole userRole:userRoles){
                String roleName=userRole.getRole().getName();
                if(roleName.equals("admin")){
                    session.setAttribute("CKFinder_UserRole", "admin");
                }else{
                    session.setAttribute("CKFinder_UserRole", "user");
                }
            }
            //end
            user.setLoginIP(token.getHost());
            //设置登录次数、时间
            userService.updateUserLogin(user);
            //ckfinder 添加权限
            return new SimpleAuthenticationInfo(new Principal(user),user.getPassword(), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = getUserService().getUser(principal.getLoginName());
		if (user != null) {
			UserUtils.putCache("user", user);
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			//把principals放session中 key=userId value=principals
			SecurityUtils.getSubject().getSession().setAttribute(String.valueOf(user.getId()),SecurityUtils.getSubject().getPrincipals());

			//赋予角色
			for(UserRole userRole:user.getUserRoles()){
				info.addRole(userRole.getRole().getName());
			}
			//赋予权限
			for(Permission permission:permissionService.getPermissions(user.getId())){
				if(StringUtils.isNotBlank(permission.getPermCode()))
					info.addStringPermission(permission.getPermCode());
			}
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 获取系统业务对象
	 */
	public UserService getUserService() {
		if (userService == null){
			userService = SpringContextHolder.getBean(UserService.class);
		}
		return userService;
	}

    /**
     * 验证码校验
     * @param token
     * @return boolean
     */
    protected boolean doCaptchaValidate(UsernamePasswordCaptchaToken token)
    {
        String captcha = (String) SecurityUtils.getSubject().getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (captcha != null &&!captcha.equalsIgnoreCase(token.getCaptcha())){
            throw new CaptchaException("验证码错误！");
        }
        return true;
    }
	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;

		private Integer id;
		private String loginName;
		private String name;
		private Map<String, Object> cacheMap;

		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
		}

		public Integer getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Map<String, Object> getCacheMap() {
			if (cacheMap==null){
				cacheMap = new HashMap<String, Object>();
			}
			return cacheMap;
		}

	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}
}
