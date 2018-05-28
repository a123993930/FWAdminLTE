package com.guy.system.areainfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.areainfo.dao.AreaInfoDao;
import com.guy.system.areainfo.entity.AreaInfo;

/**
 * 区域service
 * 
 * @author blank
 * @date 2015年5月09日
 */
@Service
@Transactional(readOnly = true)
public class AreaInfoService extends BaseService<AreaInfo, Integer> {

    @Autowired
    private AreaInfoDao areaInfoDao;

    @Override
    public HibernateDao<AreaInfo, Integer> getEntityDao() {
        return areaInfoDao;
    }

    /**
     * 通过父节点获取tree
     * 
     * @param pid
     * @return
     */
    public List<AreaInfo> getInfoByPid(int pid) {
        return areaInfoDao.getInfoByPid(pid);
    }

    /**
     * 通过代码获取id
     * 
     * @param code
     * @return
     */
    public AreaInfo getInfoByCode(String code) {
        return areaInfoDao.findUniqueBy("areaCode", code);
    }

    /**
     * 通过代码获取名称
     * 
     * @param code
     * @return
     */
    public AreaInfo getInfoByName(String name) {
        String hql = "from AreaInfo where areaName like ?0";
        List<AreaInfo> areaInfos = areaInfoDao.find(hql, "%"+name+"%");
        if(areaInfos.isEmpty()){
            return null;
        }else{
            return areaInfos.get(0);
        }
    }

    public List<AreaInfo> getHb() {
        return this.areaInfoDao.findBy("pid", 2);
    }
}
