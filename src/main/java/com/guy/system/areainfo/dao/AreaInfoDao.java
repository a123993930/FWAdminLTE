package com.guy.system.areainfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.areainfo.entity.AreaInfo;


/**
 * 区域DAO
 * @author blank
 * @date 2015年5月09日
 */
@Repository
public class AreaInfoDao extends HibernateDao<AreaInfo, Integer>{

    public List<AreaInfo> getInfoByPid(int pid) {
        String hql="from AreaInfo where pid=?0";
        List<AreaInfo> infos=null;
        try {
            infos= find(hql, pid);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return infos;
    }

}
