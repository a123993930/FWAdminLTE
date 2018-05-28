package com.guy.system.user.service;

import com.guy.common.persistence.HibernateDao;
import com.guy.common.service.BaseService;
import com.guy.common.utils.DateUtils;
import com.guy.common.utils.security.Digests;
import com.guy.common.utils.security.Encodes;
import com.guy.system.security.SystemAuthorizingRealm;
import com.guy.system.user.dao.UserDao;
import com.guy.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户service
 *
 * @author blank
 * @date 2015年1月13日
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BaseService<User, Integer> {
    @Autowired
    private SystemAuthorizingRealm systemRealm;

    /**
     * 加密方法
     */
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;    //盐长度

    @Autowired
    private UserDao userDao;

    @Override
    public HibernateDao<User, Integer> getEntityDao() {
        return userDao;
    }

    /**
     * 保存用户
     *
     * @param user
     */
    @Transactional(readOnly = false)
    public void save(User user) {
        entryptPassword(user);
        user.setCreateDate(DateUtils.getSysTimestamp());
        try {
            userDao.save(user);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * 修改密码
     *
     * @param user
     */
    @Transactional(readOnly = false)
    public void updatePwd(User user) {
        entryptPassword(user);
        userDao.save(user);
        systemRealm.clearCachedAuthorizationInfo(user.getLoginName());
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        if (!isSupervisor(id))
            userDao.delete(id);
    }

    /**
     * 按登录名查询用户
     *
     * @param loginName
     * @return 用户对象
     */
    public User getUser(String loginName) {
        return userDao.findByLoginName(loginName);
    }

    /**
     * 判断是否超级管理员
     *
     * @param id
     * @return boolean
     */
    private boolean isSupervisor(Integer id) {
        return id == 1;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    /**
     * 验证原密码是否正确
     *
     * @param user
     * @return
     */
    public boolean checkPassword(User user, String oldPassword) {
        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(oldPassword.getBytes(), salt, HASH_INTERATIONS);
        return user.getPassword().equals(Encodes.encodeHex(hashPassword));
    }

    /**
     * 修改用户登录
     *
     * @param user
     */
    @Transactional(readOnly = false)
    public void updateUserLogin(User user) {
        user.setLoginCount((user.getLoginCount() == null ? 0 : user.getLoginCount()) + 1);
        user.setPreviousVisit(user.getLastVisit());
        user.setLastVisit(DateUtils.getSysTimestamp());
        update(user);
        userDao.getSession().flush();//加入切面日志后不执行更新
    }

}
