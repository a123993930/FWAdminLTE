package com.guy.${packageName}.${objectNameLower}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.${packageName}.${objectNameLower}.dao.${objectName}Dao;
import com.guy.${packageName}.${objectNameLower}.entity.${objectName};

/**
* 类名称：${objectName}Service
* 创建人：blank
* 创建时间：${nowDate?string("yyyy-MM-dd")}
*/
@Service
@Transactional(readOnly=true)
public class ${objectName}Service extends BaseService<${objectName}, Integer> {

@Autowired
private ${objectName}Dao ${objectNameLower}Dao;

@Override
public HibernateDao<${objectName}, Integer> getEntityDao() {
return ${objectNameLower}Dao;
}
}
