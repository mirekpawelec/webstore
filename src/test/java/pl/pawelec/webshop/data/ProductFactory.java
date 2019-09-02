package pl.pawelec.webshop.data;

import pl.pawelec.webshop.model.Product;
import pl.pawelec.webshop.model.status.ProductStatus;

import java.math.BigDecimal;

public class ProductFactory {

    private static final String PRODUCT_NO = "000.000.01";
    private static final String NAME = "Smartphone";
    private static final String MANUFACTURER = "SomeManufacturer";
    private static final String CATEGORY = "Smartphone";
    private static final String DESCRIPTION = "Some description";
    private static final BigDecimal UNIT_PRICE = new BigDecimal("999.99");
    private static final int QUANTITY = 1;

    public static Product create() {
        return new Product.Builder()
                .withProductNo(PRODUCT_NO)
                .withName(NAME)
                .withManufacturer(MANUFACTURER)
                .withCategory(CATEGORY)
                .withDescription(DESCRIPTION)
                .withUnitPrice(UNIT_PRICE)
                .withQuantityInBox(QUANTITY)
                .withStatus(ProductStatus.ED.name())
                .build();
    }

    public static Product create(Long productId) {
        Product product = create();
        product.setProductId(productId);
        return product;
    }
}
