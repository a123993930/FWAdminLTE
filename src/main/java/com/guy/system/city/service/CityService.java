package com.guy.system.city.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.city.dao.CityDao;
import com.guy.system.city.entity.City;

/**
 * 类名称：CityService 创建人：blank 创建时间：2016-12-13
 */
@Service
@Transactional(readOnly = true)
public class CityService extends BaseService<City, Integer> {

    @Autowired
    private CityDao cityDao;

    @Override
    public HibernateDao<City, Integer> getEntityDao() {
        return cityDao;
    }

    public List<City> getCityByParentId(int pid) {
        return cityDao.loadCityByParentId(pid);
    }

    
    /**
     * 获取省list
     * 
     * @return
     */
    public List<City> getProvince() {
        return cityDao.loadCityByLevel(0);
    }

    /**
     * 获取市list
     * 
     * @return
     */
    public List<City> getCity() {
        return cityDao.loadCityByLevel(1);
    }

    /**
     * 获取区list
     * 
     * @return
     */
    public List<City> getDistrict() {
        return cityDao.loadCityByLevel(2);
    }
}
