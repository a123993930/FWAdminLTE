package com.guy.system.organization.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.organization.dao.OrganizationDao;
import com.guy.system.organization.entity.Organization;

/**
 * 区域service
 * @author blank
 * @date 2015年5月09日
 */
@Service
@Transactional(readOnly=true)
public class OrganizationService extends BaseService<Organization, Integer>{
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	public HibernateDao<Organization, Integer> getEntityDao() {
		return organizationDao;
	}

	/**
	 * 查询是否存在orgCode
	 * @param code
	 * @return
	 */
	public boolean isHasCode(String code){
	    boolean bool=false;
	    String hql="from Organization where orgCode =?0";
	    Organization org = organizationDao.findUnique(hql, code);
	    if(org!=null){
	        bool=true;
	    }
	    return bool;
	}

    /**
     * 通过pid获取组织机构信息
     * @param pid
     * @return
     */
    public List<Organization> getOrgByParentId(String pid) {
        List<Organization> list=organizationDao.loadOrgByParentId(pid);
        for (Organization organization : list) {
            List<Organization> cList= organizationDao.loadOrgByParentId(organization.getOrgCode());
            if(cList.size()==0){
                organization.setState("open");
            }else{
                organization.setState("closed");
            }
        }
        
        return list;
    }
}
