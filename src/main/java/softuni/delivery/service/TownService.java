package softuni.delivery.service;

import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.model.view.TownViewModel;

import java.util.List;

public interface TownService {
    TownServiceModel findByName(String name);

    void addTown(TownServiceModel townServiceModel);

    List<TownViewModel> findAllTowns();

    Town findById(String id);


    void addRestaurant(Town town, Restaurant restaurant);

    void deleteRestaurantById(String id);

    void deleteTownById(String id);
}
