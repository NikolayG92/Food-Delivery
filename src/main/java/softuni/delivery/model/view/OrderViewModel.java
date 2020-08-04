package softuni.delivery.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class OrderViewModel {

    private String customer;
    private AddressServiceModel address;
    private List<ProductViewModel> products;
    private BigDecimal totalPrice;
}
