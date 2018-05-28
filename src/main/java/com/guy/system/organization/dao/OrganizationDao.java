package com.guy.system.organization.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.organization.entity.Organization;


/**
 * 机构DAO
 * @author blank
 * @date 2015年5月09日
 */
@Repository
public class OrganizationDao extends HibernateDao<Organization, Integer>{

    public List<Organization> loadOrgByParentId(String pid) {
        String hql ="from Organization where pid = ?0";
        Query query=createQuery(hql,pid);
        return query.list();
    }
    
}
