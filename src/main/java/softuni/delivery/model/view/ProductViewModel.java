package softuni.delivery.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProductViewModel {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private int size;
    private String imageUrl;
    private int quantity;
    private CategoryViewModel category;
    private BigDecimal totalPrice;
}
