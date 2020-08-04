package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.exceptions.ActiveOrderException;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.view.RestaurantViewModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.service.RestaurantService;
import softuni.delivery.service.TownService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final TownService townService;
    private final TownRepository townRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, ModelMapper modelMapper, TownService townService, TownRepository townRepository, CategoryRepository categoryRepository, CategoryRepository categoryRepository1, OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.townRepository = townRepository;
        this.categoryRepository = categoryRepository1;
        this.orderRepository = orderRepository;
    }


    @Override
    public void addRestaurant(RestaurantServiceModel restaurantServiceModel) {

        Restaurant restaurant = this.modelMapper
                .map(restaurantServiceModel, Restaurant.class);
        this.townService.addRestaurant(this.modelMapper
                .map(restaurantServiceModel.getTown(), Town.class), restaurant);
        this.restaurantRepository.saveAndFlush(restaurant);
    }

    @Override
    public List<RestaurantViewModel> findRestaurantsByTownId(String townId) {

        Town town = this.townService
                .findById(townId);
        List<RestaurantViewModel> restaurants = this.restaurantRepository
                .findAllByTown(town)
                .stream()
                .map(restaurant -> {
                    RestaurantViewModel restaurantViewModel =
                            this.modelMapper
                            .map(restaurant, RestaurantViewModel.class);

                    return restaurantViewModel;
                }).collect(Collectors.toList());

        restaurants.sort(Comparator.comparing(RestaurantViewModel::getName));

        return restaurants;
    }

    @Override
    public Restaurant findById(String id) {

        Restaurant restaurant = this.restaurantRepository.findById(id).orElse(null);
        if(restaurant == null){
            throw new EntityNotFoundException("Restaurant with that id doesn't exist!");
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> findAllRestaurants() {

        List<Restaurant> restaurants = restaurantRepository.findAll();
        if(restaurants.size() == 0){
            throw new NullPointerException("There aren't restaurants!");
        }
        return restaurants;
    }

    @Override
    public Restaurant findByName(String restaurant) {

        Restaurant rest = this.restaurantRepository.findByName(restaurant);
        if(rest == null){
            throw new EntityNotFoundException(String.format(
                    "Restaurant with name %s doesn't exist!", restaurant));
        }
       return rest;
    }

    @Override
    public void deleteCategoryById(String id) {

        Category cat = this.categoryRepository
                .findById(id).orElse(null);
        if (cat != null && this.orderRepository.findAllByRestaurant(cat.getRestaurant()).size() == 0) {
            this.categoryRepository
                    .findById(id).ifPresent(category -> {
                this.restaurantRepository
                        .findById(category.getRestaurant().getId())
                        .ifPresent(restaurant -> restaurant.getCategory().remove(category));
            });
        }else{
            throw new ActiveOrderException("This category has active orders!");
        }

        this.categoryRepository
                .deleteById(id);
    }

    @Override
    public RestaurantServiceModel findByCategoryId(String id) {

        Category category = this.categoryRepository.findById(id).orElse(null);
        return this.modelMapper
        .map(this.restaurantRepository
                .findByCategory(category), RestaurantServiceModel.class);
    }


}
