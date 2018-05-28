package com.guy.${packageName}.${objectNameLower}.dao;

import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.${packageName}.${objectNameLower}.entity.${objectName};

/**
* 类名称：${objectName}Dao
* 创建人：blank
* 创建时间：${nowDate?string("yyyy-MM-dd")}
*/
@Repository
public class ${objectName}Dao extends HibernateDao<${objectName}, Integer>{

}
