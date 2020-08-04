package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Town;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantServiceModel extends BaseServiceModel{


    private String name;
    private TownServiceModel town;
    private String description;
    private String imageUrl;
    private Set<CategoryServiceModel> category;
}
