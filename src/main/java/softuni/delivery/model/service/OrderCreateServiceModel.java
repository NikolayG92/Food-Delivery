package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class OrderCreateServiceModel {

    private String customer;
    private AddressServiceModel address;
    private List<ProductServiceModel> products;
    private BigDecimal totalPrice;
}
