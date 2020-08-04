package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryServiceModel extends BaseServiceModel{

    private String name;
    private String imageUrl;
    private RestaurantServiceModel restaurant;

}
