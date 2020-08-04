package softuni.delivery.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Product;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CategoryViewModel {

    private String id;
    private String name;
    private List<Product> products;
}
