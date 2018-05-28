package com.guy.system.role.dao;

import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.role.entity.Role;


/**
 * 角色DAO
 * @author blank
 * @date 2015年1月13日
 */
@Repository
public class RoleDao extends HibernateDao<Role, Integer>{

}
