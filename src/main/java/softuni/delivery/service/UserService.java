package softuni.delivery.service;


import org.springframework.security.core.userdetails.UserDetails;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.model.service.UserServiceModel;

import java.util.List;

public interface UserService {

    UserServiceModel register(UserServiceModel user);

    UserServiceModel findByUsername(String username) throws UserNotFoundException;

    void editUserProfile(UserServiceModel userServiceModel, String oldPassword) throws UserNotFoundException;

    void addAddress(AddressServiceModel address);

    List<User> findAllUsers();


    UserServiceModel findById(String id);

    void addRoleToUser(UserServiceModel user, RoleServiceModel role);
}
