/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.webshop.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import pl.pawelec.webshop.exception.NoLoadunitNoException;
import pl.pawelec.webshop.exception.NoProductIdFoundException;
import pl.pawelec.webshop.model.DeliveryItem;
import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.service.DeliveryItemService;
import pl.pawelec.webshop.service.ProductService;

import java.util.Optional;

/**
 * @author mirek
 */
@Component
public class DeliveryItemValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryItemValidator.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private DeliveryItemService deliveryItemService;

    public void validateAddItemToDelivery(DeliveryItem deliveryItem, ValidationContext context) {
        DeliveryItem item = new DeliveryItem();
        Product product = null;
        boolean newItem = true;

        if (Optional.ofNullable(deliveryItem.getItemId()).isPresent()) {
            LOGGER.info("Update item");
            newItem = false;
        } else {
            LOGGER.info("New item");
        }

        try {
            item = deliveryItemService.getByLoadunitNo(deliveryItem.getLoadunitNo());
        } catch (NoLoadunitNoException nl) {
            LOGGER.info("OK! The loadunit number is not used.");
        }

        try {
            product = productService.getOneById(deliveryItem.getProduct().getProductId());
            LOGGER.info("OK! The product exists.");
        } catch (NoProductIdFoundException np) {
        }

        MessageContext messages = context.getMessageContext();
        if ((Optional.ofNullable(item.getItemId()).isPresent() && newItem) || (Optional.ofNullable(item.getItemId()).isPresent() && !item.equals(deliveryItem) && !newItem)) {
            LOGGER.info("Not unique! The loadunit number is used.");
            messages.addMessage(new MessageBuilder().error().source("loadunitNo")
                    .code("pl.pawelec.webshop.validator.DeliveryItemValidator.loadunitNo.message").build());
        }

        if (product == null) {
            LOGGER.info("Not correct! The product doesn't exist.");
            messages.addMessage(new MessageBuilder().error().source("product.productId")
                    .code("pl.pawelec.webshop.validator.DeliveryItemValidator.productId.message").build());
        }
    }
}
