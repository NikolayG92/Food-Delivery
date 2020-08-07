package softuni.delivery.service;

import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.ProductViewModel;

import java.math.BigDecimal;
import java.util.List;


public interface CartService {

    BigDecimal findTotalPrice(List<ProductViewModel> products);

    ProductViewModel findByName(String name);

    void removeProduct(ProductViewModel product, CartViewModel cart);
}
