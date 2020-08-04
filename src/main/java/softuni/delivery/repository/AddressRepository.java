package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.entity.User;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findAllByUser(User user);

    List<Address> findAllByTown(Town town);

    List<Address> deleteAllByTown(Town town);

    Address findByStreet(String street);


}
