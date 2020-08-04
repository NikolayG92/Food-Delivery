package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.*;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByInPendingIsTrue();

    List<Order> findAllByAcceptedIsTrue();

    List<Order> findAllByUser(User user);

    List<Order> findAllByAddress(Address address);

    List<Order> findAllByRestaurant(Restaurant restaurant);
    List<Order> findAllByProducts(Product product);

}
