/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.service.validator;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import pl.pawelec.webshop.model.Delivery;

/**
 * @author mirek
 */
@Component
public class DeliveryValidator {

    public void validateAddDetailsToDelivery(Delivery delivery, ValidationContext context) {
        MessageContext messages = context.getMessageContext();
        if (delivery.getPlace().getPlaceNo().equals("NONE")) {
            messages.addMessage(new MessageBuilder().error().source("place.placeNo")
                    .code("pl.pawelec.webshop.validator.DeliveryValidator.placeNo.message").build());
        }
    }
}
