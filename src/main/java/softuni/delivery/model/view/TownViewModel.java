package softuni.delivery.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Restaurant;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TownViewModel {

    private String id;
    private String name;
    private String imageUrl;
    private List<Restaurant> restaurants;
}
