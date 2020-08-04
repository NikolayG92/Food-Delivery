package softuni.delivery.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.service.ProductServiceModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CartViewModel {

    private List<ProductViewModel> products = new ArrayList<>();
    private BigDecimal totalPrice;

}
