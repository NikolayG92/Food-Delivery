package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Role;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.repository.RoleRepository;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.service.RoleService;
import softuni.delivery.service.UserService;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, AddressRepository addressRepository, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {

        if(this.userRepository.findAll().size() == 0){
            this.roleService.seedRolesInDb();
            userServiceModel.setAuthorities(this.roleService.findAllRoles());
        }
        User user = this.modelMapper
                .map(userServiceModel, User.class);

        if(this.userRepository.findAll().size() > 0){
            RoleServiceModel role = this.roleService.findByAuthority("ROLE_USER");
            user.setAuthorities(new HashSet<>());
            user.getAuthorities().add(this.modelMapper
                    .map(role, Role.class));
        }
        user.setPassword(bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) throws UserNotFoundException {

        UserServiceModel userServiceModel = this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .orElse(null);

        if(userServiceModel == null){
            throw new UserNotFoundException();
        }

        return userServiceModel;

    }

    @Override
    public void editUserProfile(UserServiceModel userServiceModel, String oldPassword) throws UserNotFoundException {
        User user = userRepository.findByUsername(userServiceModel.getUsername())
                .orElse(null);
        if(user != null){
            if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                throw new UserNotFoundException("Incorrect old password!");
            }
           ;
            user.setPassword(bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
            user.setEmail(userServiceModel.getEmail());
            user.setPhoneNumber(userServiceModel.getPhoneNumber());
            this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
        }

    }

    @Override
    public void addAddress(AddressServiceModel addressServiceModel) {

        Address address = this.modelMapper
                .map(addressServiceModel, Address.class);

        this.addressRepository.saveAndFlush(address);
    }

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public UserServiceModel findById(String id) {
        return this.modelMapper
        .map(this.userRepository
                .findById(id).orElse(null), UserServiceModel.class);
    }

    @Override
    public void addRoleToUser(UserServiceModel userServiceModel, RoleServiceModel roleServiceModel) {
        User user = this.userRepository
                .findByUsername(userServiceModel.getUsername()).orElse(null);
        Role role = this.roleRepository
                .findByAuthority(roleServiceModel.getAuthority());
        if(user != null){
            user.getAuthorities().add(role);
            this.userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .orElseThrow
                        (() ->
                                new UsernameNotFoundException(String.format("%s user not found", username)));
    }
}
