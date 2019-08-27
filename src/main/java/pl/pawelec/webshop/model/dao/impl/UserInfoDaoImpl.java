/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.model.dao.AbstrDao;
import pl.pawelec.webshop.model.dao.UserInfoDao;

import javax.persistence.NoResultException;

/**
 * @author mirek
 */
@Transactional
@Repository
public class UserInfoDaoImpl extends AbstrDao<UserInfo> implements UserInfoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoDaoImpl.class);

    @Override
    public UserInfo getByLogin(String userLogin) {
        UserInfo userInfo = null;
        try {
            userInfo = (UserInfo) getEntityManager().createQuery("from UserInfo WHERE login = :login")
                    .setParameter("login", userLogin)
                    .getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.info("The " + userLogin + " does not exist!");
        }
        return userInfo;
    }

    public UserInfo getByLogin(String userLogin, String status) {
        UserInfo userInfo = null;
        try{
            userInfo = (UserInfo) getEntityManager().createQuery("from UserInfo WHERE login = :login AND status = :status")
                        .setParameter("login", userLogin)
                        .setParameter("status", status)
                        .getSingleResult();
        } catch (NoResultException nre){
            LOGGER.info("For parameters " + userLogin + ", " + status + " no data was found!");
        }
        return userInfo;
    }

}
