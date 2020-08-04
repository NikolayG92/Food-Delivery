package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProductServiceModel extends BaseServiceModel{

    private String name;
    private int size;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private CategoryServiceModel category;
}
