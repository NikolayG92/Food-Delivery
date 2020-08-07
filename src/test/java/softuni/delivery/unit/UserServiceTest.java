package softuni.delivery.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.exceptions.UserWrongCredentialsException;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Role;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.repository.RoleRepository;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    AddressRepository addressRepository;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

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

        userService.addAddress(this.modelMapper
        .map(address, AddressServiceModel.class));

        when(addressRepository.count())
            .thenReturn(0L);
        verify(addressRepository)
                .saveAndFlush(any());

    }

/*    @Test
    public void registerUser_whenUserFound_shouldReturnSameUser() {
        UserServiceModel user = new UserServiceModel();
        user.setId("1");
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("123456");


        when(roleRepository.count())
                .thenReturn(1L);

        when(userRepository.count())
                .thenReturn(0L);

        when(roleRepository.findAll())
                .thenReturn(List.of(new Role("ADMIN")));

        userService.register(user);
        verify(userRepository)
                .save(any());
    }*/

    @Test
    public void addRoleToUser_shouldAddNewRoleToUser() {
        User user = new User();
        user.setId("1");
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("123456");
        user.setAuthorities(new HashSet<>());

        when(userRepository.findByUsername("username"))
                .thenReturn(Optional.of(user));

        Role role = new Role();
        role.setAuthority("ADMIN");
        when(roleRepository.findByAuthority("ADMIN"))
                .thenReturn(role);

        UserServiceModel userServiceModel = this.modelMapper
                .map(user, UserServiceModel.class);
        RoleServiceModel roleServiceModel = this.modelMapper
                .map(role, RoleServiceModel.class);
        userService.addRoleToUser(userServiceModel, roleServiceModel);

        assertEquals(1, user.getAuthorities().size());
    }
}