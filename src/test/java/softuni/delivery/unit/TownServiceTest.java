package softuni.delivery.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.model.view.TownViewModel;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.service.RestaurantService;
import softuni.delivery.service.TownService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TownServiceTest extends BaseTest {

    @MockBean
    TownRepository townRepository;

    @Autowired
    TownService townService;

    @Autowired
    RestaurantService restaurantService;

    @Test
    public void findByName_shouldFindCorrectTownIfItExists(){

        Town town = new Town();
        town.setName("Sofia");
        when(townRepository.findByName("Sofia"))
                .thenReturn(town);

        TownServiceModel townServiceModel = townService.findByName("Sofia");

        assertEquals(town.getName(), townServiceModel.getName());
    }

/*    @Test
    public void findByName_shouldThrowExceptionIfTownDoesntExist(){
        Town town = new Town();
        town.setName("Sofia");

       assertThrows(EntityNotFoundException.class, () ->
               townService.findByName(town.getName()), town.getName());
    }*/

    @Test
    public void findAllTowns_shouldReturnListOfAllTowns(){

        List<Town> towns = new ArrayList<>();
        towns.add(new Town());
        towns.add(new Town());

        when(townRepository.findAll())
                .thenReturn(towns);

        List<TownViewModel> allTowns = townService.findAllTowns();

        assertEquals(towns.size(), allTowns.size());
    }

    @Test
    public void findById_shouldReturnCorrectTownIfItExists(){
        Town town = new Town();
        town.setId("1");
        town.setName("Sofia");

        when(townRepository.findById("1"))
                .thenReturn(Optional.of(town));

        Town testTown = townService.findById("1");

        assertEquals(town.getName(), testTown.getName());
    }

 /*   @Test
    public void addRestaurant_shouldAddRestaurantIfTownAndRestaurantExists(){

        Town town = new Town();
        town.setId("1");
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        town.setRestaurants(new ArrayList<>());
        town.getRestaurants().add(restaurant);

        when(townRepository.findById("1"))
                .thenReturn(Optional.of(town));
        townService.addRestaurant(town, restaurant);

        assertEquals(1, town.getRestaurants().size());
    }
*/

    @Test
    public void deleteTownById_shouldDeleteTownIfItExists(){
        Town town = new Town();
        town.setId("1");

        when(townRepository.findById("1"))
                .thenReturn(Optional.of(town));

        townService.deleteTownById("1");

        assertEquals(0, townRepository.findAll().size());
    }

  /*  @Test
    public void deleteRestaurantById_shouldDeleteRestaurantIfItExists(){
        Town town = new Town();
        town.setId("1");
        town.setName("Sofia");
        when(townRepository.findById("1"))
                .thenReturn(Optional.of(town));
        Restaurant restaurant = new Restaurant();
        restaurant.setId("1");
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId("2");

        townService.addRestaurant(town, restaurant);
        townService.addRestaurant(town, restaurant1);



        townService.deleteRestaurantById("1");

        assertEquals(1, restaurantService.findRestaurantsByTownId("1").size());
    }*/

}
