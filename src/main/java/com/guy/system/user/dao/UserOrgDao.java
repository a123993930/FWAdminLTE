package com.guy.system.user.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.user.entity.UserOrg;


/**
 * 用户机构Dao
 * @author blank
 * @date 2015年5月9日 
 */
@Repository
public class UserOrgDao extends HibernateDao<UserOrg, Integer>{

	/**
	 * 删除用户机构
	 * @param userId
	 * @param orgId
	 */
	public void deleteUO(Integer userId){
		String hql="delete UserOrg uo where uo.user.id=?0 ";
		batchExecute(hql, userId);
	}
	
	/**
	 * 查询用户拥有的机构id集合
	 * @param userId
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findOrgIds(Integer userId){
		String hql="select uo.organization.id from UserOrg uo where uo.user.id=?0";
		Query query= createQuery(hql, userId);
		return query.list();
	}

	public void deleteUO(Integer userId, Integer orgId) {
		String hql="delete UserOrg uo where uo.user.id=?0 and uo.organization.id=?1";
		batchExecute(hql, userId,orgId);
	}
	
}
