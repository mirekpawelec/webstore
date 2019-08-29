/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.pawelec.webshop.model.LoadUnit;
import pl.pawelec.webshop.repository.AbstrRepository;
import pl.pawelec.webshop.repository.LoadUnitRepository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoadUnitRepositoryImpl extends AbstrRepository<LoadUnit> implements LoadUnitRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadUnitRepositoryImpl.class);

    public List<LoadUnit> getByStatus(String status) {
        return getEntityManager().createQuery("from Repository WHERE status = :status").setParameter("status", status).getResultList();
    }

    public LoadUnit getByLoadunitNo(String loadunitNo) {
        return (LoadUnit) getEntityManager().createQuery("from Repository WHERE loadunit_no = :loadunit_no").setParameter("loadunit_no", loadunitNo).getSingleResult();
    }

    public List<LoadUnit> getByProductNo(String productNo) {
        return getEntityManager().createQuery("from Repository WHERE product.productNo = :productNo").setParameter("productNo", productNo).getResultList();
    }

    public List<LoadUnit> getByOwnCriteria(String sqlQuery, String modificationDate, String createDate) {
        List<LoadUnit> result = new ArrayList<LoadUnit>();
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
