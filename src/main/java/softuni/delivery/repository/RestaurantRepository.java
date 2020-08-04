package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

     Restaurant findByName(String name);

     List<Restaurant> findAllByTown(Town town);
     Restaurant findByCategory(Category category);
}
