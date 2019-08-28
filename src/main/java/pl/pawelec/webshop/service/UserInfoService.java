/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pawelec.webshop.model.UserInfo;

/**
 * @author mirek
 */
public interface UserInfoService extends UserDetailsService, CrudService<UserInfo> {

    UserInfo getByLogin(String userLogin);
}
