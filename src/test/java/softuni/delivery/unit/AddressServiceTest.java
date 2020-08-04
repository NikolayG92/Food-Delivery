package softuni.delivery.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.service.AddressService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AddressServiceTest extends BaseTest {

    @MockBean
    AddressRepository addressRepository;

    @Autowired
    AddressService addressService;

/*    @Test
    public void findById_shouldFindAddressById(){

       Address address = new Address();
       address.setStreet("Vitoshka");

       AddressServiceModel addressServiceModel = addressService.findById(address.getId());

       assertEquals("Vitoshka", addressServiceModel.getStreet());

    }*/


}
