package softuni.delivery.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.exceptions.UserWrongCredentialsException;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    public void getUserByUsername_whenUserExists_shouldGetUserUsername() throws UserNotFoundException {
        User user = new User();
        user.setUsername("Ivan");
        when(userRepository.findByUsername("Ivan"))
                .thenReturn(Optional.of(user));


        UserServiceModel userServiceModel = userService.findByUsername("Ivan");


        assertEquals("Ivan", userServiceModel.getUsername());
    }

    @Test
    public void getUserByUsername_whenUserDoNotExists_shouldThrowException() {
        User user = new User();
        user.setUsername("Gosho");

        assertThrows(UserNotFoundException.class, () ->
                userService.findByUsername(user.getUsername()), user.getUsername());
    }


    @Test
    public void getAllUsers_whenExist_shouldReturnAllUsersInDB() {

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll())
                .thenReturn(users);

        List<User> allUsers = userService.findAllUsers();

        assertEquals(2, allUsers.size());

    }

    @Test
    public void getAllUsers_whenEmpty_shouldReturnEmptyCollection() {
        when(userRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<User> userServiceModels = userService.findAllUsers();

        assertEquals(0, userServiceModels.size());

    }


    @Test
    public void getUserById_whenUserExists_shouldGetUser() {
        User user = new User();
        user.setId("1");
        when(userRepository.findById("1"))
                .thenReturn(Optional.of(user));


        UserServiceModel userServiceModel = userService.findById("1");


        assertEquals("1", userServiceModel.getId());
    }


    @Test
    public void getUserById_whenUserDoNotExists_shouldThrowException() {
        User user = new User();
        user.setId("1");


        assertThrows(Exception.class, () ->
                userService.findById(user.getId()), user.getId());
    }





        @Test
    public void editUserProfile_shouldEditAllFieldsOfTheUser_whenDataIsValid() throws UserNotFoundException {

        User user = new User();
        user.setUsername("Ivan");


        when(userRepository.findByUsername("Ivan"))
                .thenReturn(Optional.of(user));

        user.setPassword("1234");

        UserServiceModel editedServiceModel = new UserServiceModel();
        editedServiceModel.setPassword("asdasd");
        editedServiceModel.setEmail("ivan@mail.bg");


        UserServiceModel newUserModel = new UserServiceModel();
        userService.editUserProfile(editedServiceModel, "1234");
        newUserModel = editedServiceModel;


        assertEquals("ivan@mail.bg", newUserModel.getEmail());
        assertEquals("asdasd",newUserModel.getPassword());
    }

    @Test
    public void addAddress_shouldAddAddressToUser_whenDataIsValid(){

        User user = new User();
        user.setId("1");
        user.setUsername("pesho");
        Town town = new Town();
        town.setName("Sofia");
        Address address = new Address();
        address.setTown(town);
        address.setStreet("Vitoshka");
        address.setUser(user);

        assertEquals(address.getUser().getId(), user.getId());

    }

}
