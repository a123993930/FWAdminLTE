package com.guy.system.user.service;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.role.entity.Role;
import com.guy.system.user.dao.UserRoleDao;
import com.guy.system.user.entity.User;
import com.guy.system.user.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色service
 * @author blank
 * @date 2015年1月14日
 */
@Service
@Transactional(readOnly = true)
public class UserRoleService extends BaseService<UserRole, Integer> {

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public HibernateDao<UserRole, Integer> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 添加修改用户角色
	 * 
	 * @param id
	 * @param oldList
	 * @param newList
	 */
	@Transactional(readOnly = false)
	public void updateUserRole(Integer userId, List<Integer> oldList,List<Integer> newList) {
		// 是否删除
		for (int i = 0, j = oldList.size(); i < j; i++) {
			if (!newList.contains(oldList.get(i))) {
				userRoleDao.deleteUR(userId, oldList.get(i));
			}
		}

		// 是否添加
		for (int m = 0, n = newList.size(); m < n; m++) {
			if (!oldList.contains(newList.get(m))) {
				userRoleDao.save(getUserRole(userId, newList.get(m)));
			}
		}
	}

	/**
	 * 构建UserRole
	 * 
	 * @param userId
	 * @param roleId
	 * @return UserRole
	 */
	private UserRole getUserRole(Integer userId, Integer roleId) {
		UserRole ur = new UserRole();
		ur.setUser(new User(userId));
		ur.setRole(new Role(roleId));
		return ur;
	}

	/**
	 * 获取用户拥有角色id集合
	 * 
	 * @param userId
	 * @return 结果集合
	 */
	public List<Integer> getRoleIdList(Integer userId) {
		return userRoleDao.findRoleIds(userId);
	}
	
	/**
	 * 判断是否为普通用户
	 * @param user
	 * @return
	 */
	public boolean isNormal(User user) {
		boolean bool = false;
		List<Integer> roles = getRoleIdList(user.getId());
		if (roles.size() > 1) {
			bool = false;
		} else {
			for (Integer roleid : roles) {//
				bool = roleid != 1;
			}
		}

		return bool;
	}

}
