package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.TownServiceModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, String> {
    Town findByName(String name);

    /*List<Restaurant> findAllById(String id);*/
}
