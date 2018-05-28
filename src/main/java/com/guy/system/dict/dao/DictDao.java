package com.guy.system.dict.dao;

import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.dict.entity.Dict;

/**
 * 字典DAO
 * @author blank
 * @date 2015年1月13日
 */
@Repository
public class DictDao extends HibernateDao<Dict, Integer>{

}
