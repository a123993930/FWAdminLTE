package com.guy.system.station.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.station.dao.StationDao;
import com.guy.system.station.entity.Station;

import java.util.List;

/**
 * 类名称：StationService
 * 创建人：blank
 * 创建时间：2018-01-19
 */
@Service
@Transactional(readOnly = true)
public class StationService extends BaseService<Station, Integer> {

    @Autowired
    private StationDao stationDao;

    @Override
    public HibernateDao<Station, Integer> getEntityDao() {
        return stationDao;
    }

    /**
     * 通过名称获取职位
     * @param name
     * @return
     */
    public Station getByName(String name) {
        List<Station> list = stationDao.find("from Station where name=?0", name);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
