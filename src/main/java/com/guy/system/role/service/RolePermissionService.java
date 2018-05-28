package com.guy.system.role.service;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.permission.entity.Permission;
import com.guy.system.role.dao.RolePermissionDao;
import com.guy.system.role.entity.Role;
import com.guy.system.role.entity.RolePermission;
import com.guy.system.security.SystemAuthorizingRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限service
 * @author blank
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly=true)
public class RolePermissionService extends BaseService<RolePermission, Integer> {

	@Autowired
	private RolePermissionDao rolePermissionDao;

	@Override
	public HibernateDao<RolePermission, Integer> getEntityDao() {
		return rolePermissionDao;
	}

	/**
	 * 获取角色权限id集合
	 * @return List
	 */
	public List<Integer> getPermissionIds(Integer roleId){
		return rolePermissionDao.findPermissionIds(roleId);
	}

	/**
	 * 修改角色权限
	 * @param id
	 * @param oldList
	 * @param newList
	 */
	@Transactional(readOnly = false)
	public void updateRolePermission(Integer id,List<Integer> oldList,List<Integer> newList){
		//是否删除
		for(int i=0,j=oldList.size();i<j;i++){
			if(!newList.contains(oldList.get(i))){
				rolePermissionDao.deleteRP(id,oldList.get(i));
			}
		}

		//是否添加
		for(int i=0,j=newList.size();i<j;i++){
			if(!oldList.contains(newList.get(i))){
				rolePermissionDao.save(getRolePermission(id,newList.get(i)));
			}
		}
	}

	/**
	 * 清空该角色用户的权限缓存
	 */
	public void clearUserPermCache(PrincipalCollection pc){
		RealmSecurityManager securityManager =  (RealmSecurityManager) SecurityUtils.getSecurityManager();
		SystemAuthorizingRealm userRealm = (SystemAuthorizingRealm) securityManager.getRealms().iterator().next();
		userRealm.clearCachedAuthorizationInfo(pc);
	}

	/**
	 * 构造角色权限对象
	 * @param roleId
	 * @param permissionId
	 * @return RolePermission
	 */
	private RolePermission getRolePermission(Integer roleId,Integer permissionId){
		RolePermission rp=new RolePermission();
		rp.setRole(new Role(roleId));
		rp.setPermission(new Permission(permissionId));
		return rp;
	}

}
