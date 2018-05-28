package com.guy.system.user.dao;

import org.springframework.stereotype.Repository;

import com.guy.common.persistence.HibernateDao;
import com.guy.system.user.entity.User;


/**
 * 用户DAO
 * @author blank
 * @date 2015年1月13日
 */
@Repository
public class UserDao extends HibernateDao<User, Integer>{

    public void updateDD(User user) {
        batchExecute("update User set dingId=?0 where id=?1 ",user.getDingId(),user.getId());
    }

    /**
     * 通过
     * @param loginName
     * @return
     */
    public User findByLoginName(String loginName) {
        return findUniqueBy("loginName", loginName);
    }
}
