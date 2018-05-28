package com.guy.system.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.organization.entity.Organization;
import com.guy.system.user.dao.UserOrgDao;
import com.guy.system.user.entity.User;
import com.guy.system.user.entity.UserOrg;

/**
 * 用户机构Service
 * @author blank
 * @date 2015年5月9日 
 */
@Service
@Transactional(readOnly = true)
public class UserOrgService extends BaseService<UserOrg, Integer> {

	@Autowired
	private UserOrgDao userOrgDao;

	@Override
	public HibernateDao<UserOrg, Integer> getEntityDao() {
		return userOrgDao;
	}

	/**
	 * 添加修改用户机构
	 * 
	 * @param id
	 * @param oldList
	 * @param newList
	 */
	@Transactional(readOnly = false)
	public void updateUserOrg(Integer userId, List<Integer> oldList,List<Integer> newList) {
		
			// 是否删除
			for (int i = 0, j = oldList.size(); i < j; i++) {
				if (!newList.contains(oldList.get(i))) {
					userOrgDao.deleteUO(userId, oldList.get(i));
				}
			}

			// 是否添加
			for (int m = 0, n = newList.size(); m < n; m++) {
				if (!oldList.contains(newList.get(m))) {
					userOrgDao.save(getUserOrg(userId, newList.get(m)));
				}
			}
//		// 删除老的机构关系
//		userOrgDao.deleteUO(user.getId());
//		// 添加新的机构关系
//		for (Organization organization : organizations) {
//			userOrgDao.save(new UserOrg(user, organization));
//		}
	}

	/**
	 * 获取用户拥有机构id集合
	 * 
	 * @param userId
	 * @return 结果集合
	 */
	public List<Integer> getOrgIdList(Integer userId) {
		return userOrgDao.findOrgIds(userId);
	}
	
	/**
	 * 构建UserRole
	 * 
	 * @param userId
	 * @param roleId
	 * @return UserRole
	 */
	public UserOrg getUserOrg(Integer userId, Integer orgId) {
		UserOrg uo = new UserOrg();
		uo.setUser(new User(userId));
		uo.setOrganization(new Organization(orgId));
		return uo;
	}

}
