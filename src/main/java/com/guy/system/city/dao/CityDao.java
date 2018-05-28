package com.guy.system.city.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.city.entity.City;

/** 
 * 类名称：CityDao
 * 创建人：blank 
 * 创建时间：2016-12-13
 */
@Repository
public class CityDao extends HibernateDao<City, Integer>{
    /**
     * 通过 级别查询
     * @param level
     * @return
     */
    public List<City> loadCityByLevel(Integer level){
        String hql ="from City where level = ?0";
        Query query=createQuery(hql,level);
        return query.list();
    }

    /**
     * 通过父级id获取子节点
     * @param pid
     * @return
     */
    public List<City> loadCityByParentId(Integer pid) {
        String hql ="from City where parentId = ?0";
        Query query=createQuery(hql,pid);
        return query.list();
    }
}
