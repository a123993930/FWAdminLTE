package com.guy.system.dict.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.system.dict.dao.DictDao;
import com.guy.system.dict.entity.Dict;

/**
 * 字典service
 * @author blank
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly=true)
public class DictService extends BaseService<Dict, Integer> {
	
	@Autowired
	private DictDao dictDao;

	@Override
	public HibernateDao<Dict, Integer> getEntityDao() {
		return dictDao;
	}
}
