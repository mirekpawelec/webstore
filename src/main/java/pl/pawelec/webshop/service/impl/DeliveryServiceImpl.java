/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.webflow.execution.RequestContext;
import pl.pawelec.webshop.service.exception.InvalidDeliveryException;
import pl.pawelec.webshop.model.Delivery;
import pl.pawelec.webshop.model.Storageplace;
import pl.pawelec.webshop.repository.DeliveryRepository;
import pl.pawelec.webshop.model.status.DeliveryStatus;
import pl.pawelec.webshop.service.DeliveryService;
import pl.pawelec.webshop.service.StorageplaceService;
import pl.pawelec.webshop.service.utils.TimeUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository deliveryDao;

    @Autowired
    private StorageplaceService storageplaceService;

    @Transactional
    @Override
    public void create(Delivery delivery) {
        deliveryDao.create(delivery);
    }

    @Transactional
    @Override
    public void update(Delivery delivery) {
        deliveryDao.update(delivery);
    }

    @Transactional
    @Override
    public void delete(Delivery delivery) {
        if (delivery.getStatus().equals(DeliveryStatus.OK.name())) {
            deliveryDao.delete(delivery);
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        deliveryDao.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteAll() {
        deliveryDao.deleteAll();
    }

    @Override
    public Delivery getOneById(Long id) {
        return deliveryDao.getOneById(id);
    }

    @Override
    public List<Delivery> getAll() {
        return deliveryDao.getAll();
    }

    @Override
    public Long count() {
        return deliveryDao.count();
    }

    @Override
    public boolean exists(Long id) {
        return deliveryDao.exists(id);
    }

    @Override
    public List<Delivery> getByDriver(String firstName, String lastName, String phoneNo) {
        return deliveryDao.getByDriver(firstName, lastName, phoneNo);
    }

    @Override
    public List<Delivery> getByTruck(String type, String truckNumber, String trailerOrCaravanNumber) {
        return deliveryDao.getByTruck(type, truckNumber, trailerOrCaravanNumber);
    }

    @Transactional
    @Override
    public Delivery startProcessDelivery(String deliveryId) {
        Delivery delivery = new Delivery();
        if (deliveryId == null) {
            delivery = deliveryDao.startProcessDelivery();
        } else {
            delivery = deliveryDao.getOneById(Long.valueOf(deliveryId));
        }
        if (delivery == null) {
            throw new InvalidDeliveryException("It has occured an error while creating a delivery!");
        }
        return delivery;
    }

    @Transactional
    @Override
    public String closeDelivery(Long id) {
        String result = "true";
        Delivery deliveryToClose = deliveryDao.getOneById(id);
        if (Objects.nonNull(deliveryToClose) && deliveryToClose.getStatus().equals(DeliveryStatus.RE.name())) {
            deliveryToClose.setStatus(DeliveryStatus.FI.name());
            deliveryToClose.setFinishDate(TimeUtils.getCurrentLocalDateTime());
            deliveryDao.update(deliveryToClose);
        } else {
            result = "false";
        }
        return result;
    }

    @Override
    public Delivery setPlaceIdAccordingToPlaceNo(Delivery delivery, List<Storageplace> storageplaces) {
        storageplaces.forEach(storageplace -> {
            if (storageplace.getPlaceNo().equals(delivery.getPlace().getPlaceNo())) {
                delivery.getPlace().setPlaceId(storageplace.getPlaceId());
            }
        });
        return delivery;
    }

    @Transactional
    @Override
    public String saveDetailsDelivery(Delivery delivery) {
        try {
            if (delivery.getPlace().getPlaceNo() != null && !delivery.getPlace().getPlaceNo().equals("NONE")) {
                delivery.getPlace().setPlaceId(storageplaceService.getByPlaceNo(delivery.getPlace().getPlaceNo()).getPlaceId());
            }
            if (!delivery.getStatus().equals("FI")) {
                delivery.setStatus(DeliveryStatus.RE.name());
                this.update(delivery);
            } else {
                throw new IllegalArgumentException("It can't update closed delivery!");
            }
        } catch (Exception e) {
            return "false";
        }
        return "true";
    }

    @Transactional
    @Override
    public void deleteByIdAndStatus(Long id, String status) {
        if (status.equals("OK")) {
            deliveryDao.deleteById(id);
        }
    }

    @Override
    public String setWhereComeFrom(String view) {
        return view;
    }

    @Override
    public String whatView(String view) {
        return view;
    }

    @Override
    public void setFlowModelAttribute(RequestContext context) {
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getNativeRequest();
        String url = context.getFlowExecutionUrl();
        url = url.substring(url.indexOf("/", 1), url.length()) + "&";
        req.getSession().setAttribute("lastRequestUrl", url);
    }

}
