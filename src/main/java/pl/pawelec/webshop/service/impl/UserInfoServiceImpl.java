/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.pawelec.webshop.model.UserDetailsAdapter;
import pl.pawelec.webshop.model.UserInfo;
import pl.pawelec.webshop.model.dao.UserInfoDao;
import pl.pawelec.webshop.model.statuses.UserStatus;
import pl.pawelec.webshop.service.UserInfoService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private UserInfoDao userInfoDao;

    @Transactional
    @Override
    public void create(UserInfo userInfo) {
        userInfoDao.create(userInfo);
    }

    @Transactional
    @Override
    public void update(UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

    @Transactional
    @Override
    public void delete(UserInfo userInfo) {
        userInfoDao.delete(userInfo);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userInfoDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        userInfoDao.deleteAll();
    }

    @Override
    public UserInfo getOneById(Long id) {
        return userInfoDao.getOneById(id);
    }

    @Override
    public List<UserInfo> getAll() {
        return userInfoDao.getAll();
    }

    @Override
    public Long count() {
        return userInfoDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return userInfoDao.exists(id);
    }

    @Override
    public UserInfo getByLogin(String userLogin) {
        return userInfoDao.getByLogin(userLogin);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo activeUserInfo = userInfoDao.getByLogin(username, UserStatus.OK.name());
        if (activeUserInfo == null) {
            LOGGER.info("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new UserDetailsAdapter(activeUserInfo);
    }
}
