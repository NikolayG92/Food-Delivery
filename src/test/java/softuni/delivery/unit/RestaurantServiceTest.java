package softuni.delivery.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ModelMap;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.model.view.RestaurantViewModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.service.CategoryService;
import softuni.delivery.service.RestaurantService;
import softuni.delivery.service.TownService;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends BaseTest {

    @MockBean
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TownService townService;

    @MockBean
    TownRepository townRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Test
    public void findById_shouldFindCorrectRestaurantIfItExists() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        restaurant.setName("Sofia");

        when(restaurantRepository.findById("1"))
                .thenReturn(Optional.of(restaurant));

        Restaurant testRestaurant = restaurantService.findById("1");

        assertEquals(restaurant.getName(), testRestaurant.getName());
    }

    @Test
    public void findById_shouldThrowExceptionIfDoesntExist() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");

        assertThrows(EntityNotFoundException.class, () ->
                restaurantService.findById(restaurant.getId()), restaurant.getId());

    }

    @Test
    public void findAllRestaurants_shouldFindAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = new Restaurant();
        Restaurant restaurant1 = new Restaurant();
        restaurants.add(restaurant);
        restaurants.add(restaurant1);

        when(restaurantRepository.findAll())
                .thenReturn(restaurants);

        assertEquals(2, restaurantService.findAllRestaurants().size());
    }

    @Test
    public void findAllRestaurants_shouldThrowExceptionIfThereArentRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        when(restaurantRepository.findAll())
                .thenReturn(restaurants);

        assertThrows(NullPointerException.class, () -> {
            restaurantService.findAllRestaurants();
        });
    }

    @Test
    public void findByName_shouldReturnRestaurantIfExists() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sps");
        when(restaurantRepository.findByName("Sps"))
                .thenReturn(restaurant);
        Restaurant testRestaurant = restaurantService.findByName(restaurant.getName());

        assertEquals(restaurant.getName(), testRestaurant.getName());
    }

    @Test
    public void findByName_shouldThrowExceptionIfDoesntExist() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Sps");

        assertThrows(EntityNotFoundException.class, () ->
                restaurantService.findByName(restaurant.getName()), restaurant.getName());
    }

    @Test
    public void findRestaurantsByTownId_shouldReturnRestaurantsByTown() {
        Town town = new Town();
        town.setId("1");
        Restaurant restaurant = new Restaurant();
        Restaurant restaurant1 = new Restaurant();
        townService.addRestaurant(town, restaurant);
        townService.addRestaurant(town, restaurant1);

        when(restaurantRepository.findAllByTown(town))
                .thenReturn(town.getRestaurants());

        assertEquals(2, restaurantService.findRestaurantsByTownId(town.getId()).size());
    }

    /*    @Test
        public void findRestaurantsByTownId_shouldReturnCollectionOfRestaurants(){
            Town town = new Town();
            town.setId("1");

            town.setRestaurants(new ArrayList<>());
            town.getRestaurants().add(new Restaurant());
            town.getRestaurants().add(new Restaurant());
            List<Restaurant> restaurants = town.getRestaurants();
            when(restaurantRepository.findAllByTown(town))
                    .thenReturn(restaurants);
            when(townRepository.findById("1"))
                    .thenReturn(Optional.of(town));

            List<RestaurantViewModel> restaurantServiceModels =
                    restaurantService.findRestaurantsByTownId("1");

            assertEquals(2, restaurantServiceModels.size());
        }*/
/*    @Test
    public void findRestaurantsByCategoryId_shouldReturnCollectionOfRestaurants() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        Category category = new Category();
        category.setId("1");
        Category category1 = new Category();
        category1.setId("2");
        restaurant.setCategory(new HashSet<>());
        restaurant.getCategory().add(category);
        restaurant.getCategory().add(category1);

        when(restaurantRepository.findById("1"))
                .thenReturn(Optional.of(restaurant));

        when(categoryRepository.findById("1"))
                .thenReturn(Optional.of(category));

        RestaurantServiceModel restaurantServiceModel = restaurantService.findByCategoryId("1");

        assertEquals(restaurant.getId(), restaurantServiceModel.getId());

    }*/

}
