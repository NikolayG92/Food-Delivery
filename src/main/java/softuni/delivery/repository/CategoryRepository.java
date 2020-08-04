package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.Category;
import softuni.delivery.model.entity.Restaurant;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findAllByRestaurant(Restaurant restaurant);

    Category findByNameAndRestaurant(String name, Restaurant restaurant);
}
