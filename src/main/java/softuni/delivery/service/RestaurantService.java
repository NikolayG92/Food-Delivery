package softuni.delivery.service;

import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.view.RestaurantViewModel;

import java.util.List;

public interface RestaurantService {

    void addRestaurant(RestaurantServiceModel restaurant);
    List<RestaurantViewModel> findRestaurantsByTownId(String townId);

    Restaurant findById(String id);

   /* void deleteRestaurant(String id);*/

    List<Restaurant> findAllRestaurants();

    Restaurant findByName(String restaurant);

    void deleteCategoryById(String id);

    RestaurantServiceModel findByCategoryId(String id);
}
