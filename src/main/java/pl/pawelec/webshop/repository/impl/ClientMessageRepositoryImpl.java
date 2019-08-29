/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.ClientMessage;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.ClientMessageRepository;

@Repository
public class ClientMessageRepositoryImpl extends AbstrRepository<ClientMessage> implements ClientMessageRepository {
}
