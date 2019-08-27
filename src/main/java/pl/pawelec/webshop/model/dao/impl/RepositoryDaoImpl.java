/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.model.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.pawelec.webshop.model.Repository;
import pl.pawelec.webshop.model.dao.AbstrDao;
import pl.pawelec.webshop.model.dao.RepositoryDao;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mirek
 */
@org.springframework.stereotype.Repository
public class RepositoryDaoImpl extends AbstrDao<Repository> implements RepositoryDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryDaoImpl.class);

    public List<Repository> getByStatus(String status) {
        return getEntityManager().createQuery("from Repository WHERE status = :status").setParameter("status", status).getResultList();
    }

    public Repository getByLoadunitNo(String loadunitNo) {
        return (Repository) getEntityManager().createQuery("from Repository WHERE loadunit_no = :loadunit_no").setParameter("loadunit_no", loadunitNo).getSingleResult();
    }

    public List<Repository> getByProductNo(String productNo) {
        return getEntityManager().createQuery("from Repository WHERE product.productNo = :productNo").setParameter("productNo", productNo).getResultList();
    }

    public List<Repository> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate) {
        List<Repository> result = new ArrayList<Repository>();
        Query query;
        try {
            if (!modificationDate.isEmpty() && createDate.isEmpty()) {
                sqlQuery += " lastModificationDate = ?1";
                query = getEntityManager().createQuery(sqlQuery);
                query.setParameter(1, LocalDateTime.parse(modificationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else if (modificationDate.isEmpty() && !createDate.isEmpty()) {
                sqlQuery += " createDate = ?1";
                query = getEntityManager().createQuery(sqlQuery);
                query.setParameter(1, LocalDateTime.parse(createDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else if (!modificationDate.isEmpty() && !createDate.isEmpty()) {
                sqlQuery += " lastModificationDate = ?1 AND createDate = ?2";
                query = getEntityManager().createQuery(sqlQuery);
                query.setParameter(1, LocalDateTime.parse(modificationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .setParameter(2, LocalDateTime.parse(createDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                query = getEntityManager().createQuery(sqlQuery);
            }
            ;
//            System.out.println(sqlQuery);
            result = query.getResultList();
        } catch (NoResultException nre) {
            LOGGER.info("No data found!");
        } catch (DateTimeParseException pe) {
            LOGGER.info("it has occurred an conversion error a date! " + pe.getMessage());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return result;
    }

}
