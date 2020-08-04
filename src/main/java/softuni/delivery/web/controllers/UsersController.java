package softuni.delivery.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.delivery.config.annotations.PageTitle;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.binding.admin.RoleChangeBindingModel;
import softuni.delivery.model.binding.users.AddAddressBindingModel;
import softuni.delivery.model.binding.users.UserEditBindingModel;
import softuni.delivery.model.binding.users.UserRegisterBindingModel;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.service.RoleService;
import softuni.delivery.service.TownService;
import softuni.delivery.service.UserService;
import softuni.delivery.validations.user.AddAddressValidator;
import softuni.delivery.validations.user.UserEditValidator;
import softuni.delivery.validations.user.UserRegisterValidator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UsersController extends BaseController{

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TownService townService;
    private final RoleService roleService;
    private final UserRegisterValidator userRegisterValidator;
    private final UserEditValidator userEditValidator;
    private final AddAddressValidator addAddressValidator;

    public UsersController(UserService userService, ModelMapper modelMapper, UserRepository userRepository, TownService townService, RoleService roleService, UserRegisterValidator userRegisterValidator, UserEditValidator userEditValidator, AddAddressValidator addAddressValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.townService = townService;
        this.roleService = roleService;
        this.userRegisterValidator = userRegisterValidator;
        this.userEditValidator = userEditValidator;
        this.addAddressValidator = addAddressValidator;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Login")
    public ModelAndView login (){
        return view("login");
    }


    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Register")
    public String getRegister(Model model){
        if(!model.containsAttribute("userRegisterBindingModel")){
            model.addAttribute("userRegisterBindingModel", new UserRegisterBindingModel());
        }

        return "register";
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel")
                           UserRegisterBindingModel userRegisterBindingModel,
                                  BindingResult bindingResult,
                                  ModelAndView modelAndView){
        userRegisterValidator.validate(userRegisterBindingModel, bindingResult);

        if(bindingResult.hasErrors()){
            userRegisterBindingModel.setPassword(null);
            userRegisterBindingModel.setConfirmPassword(null);
            modelAndView.addObject("userRegisterBindingModel", userRegisterBindingModel);
            return view("register", modelAndView);
        }
         this.userService.register(this.modelMapper
                 .map(userRegisterBindingModel,UserServiceModel.class));

         modelAndView.setViewName("login");
         return modelAndView;
    }


    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit Profile")
    public ModelAndView editUser(Principal principal, ModelAndView modelAndView,
                                 @ModelAttribute(name = "userEditBindingModel")
            UserEditBindingModel userEditBindingModel) throws UserNotFoundException {
        UserServiceModel userServiceModel = this.userService.findByUsername(principal.getName());
        userEditBindingModel = modelMapper.map(userServiceModel, UserEditBindingModel.class);
        userEditBindingModel.setPassword(null);
        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("userEditBindingModel", userEditBindingModel);
        modelAndView.setViewName("user/edit-user");
        return modelAndView;
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@Valid @ModelAttribute(name = "userEditBindingModel")
                                                   UserEditBindingModel userEditBindingModel,
                                           BindingResult bindingResult,
                                           ModelAndView modelAndView) throws UserNotFoundException {
        userEditValidator.validate(userEditBindingModel, bindingResult);

        if (bindingResult.hasErrors()) {
            userEditBindingModel.setOldPassword(null);
            userEditBindingModel.setPassword(null);
            userEditBindingModel.setConfirmPassword(null);
            modelAndView.addObject("userEditBindingModel", userEditBindingModel);
            return view("user/edit-user", modelAndView);
        }
        UserServiceModel userServiceModel = this.modelMapper.map(userEditBindingModel, UserServiceModel.class);
        this.userService.editUserProfile(userServiceModel, userEditBindingModel.getOldPassword());
        modelAndView.addObject("towns", this.townService
                .findAllTowns());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/add/address")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Add address")
    public ModelAndView addAddress(Model model, ModelAndView modelAndView)
                                  {
        if(!model.containsAttribute("addAddressBindingModel")){
            model.addAttribute("addAddressBindingModel", new AddAddressBindingModel());
        }


        modelAndView.addObject("towns", this.townService.findAllTowns());
        modelAndView.setViewName("user/add-address");
        return modelAndView;
    }

    @PostMapping("/add/address")
    @PreAuthorize("isAuthenticated()")
    public String addAddressConfirm(@Valid @ModelAttribute("addAddressBindingModel")
                                    AddAddressBindingModel addAddressBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    Principal principal) throws UserNotFoundException {
        addAddressValidator.validate(addAddressBindingModel, bindingResult);
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addAddressBindingModel", addAddressBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.BindingResult.addAddressBindingModel", bindingResult);
            return "user/add-address";
        }


        UserServiceModel user = this.userService
                .findByUsername(principal.getName());
       AddressServiceModel address = this.modelMapper
               .map(addAddressBindingModel, AddressServiceModel.class);
        TownServiceModel town = this.townService.findByName(addAddressBindingModel.getTown());
        address.setTown(town);
        address.setUser(user);
        this.userService.addAddress(this.modelMapper
        .map(address, AddressServiceModel.class));
        return "redirect:/home";
    }

    @GetMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PageTitle("All Users")
    public String getAllUsers(Model model){

        if (!model.containsAttribute("roleChangingBindingModel")){
            model.addAttribute("roleChangingBindingModel", new RoleChangeBindingModel());
        }
        model.addAttribute("users", this.userService.findAllUsers());
        return "admin/all-users";
    }

    @PostMapping("/all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView changeUserRole(@RequestParam("id") String id,
                                       ModelAndView modelAndView,
                                       RoleChangeBindingModel roleChangeBindingModel){

        UserServiceModel user = this.userService
                .findById(id);

        RoleServiceModel role = new RoleServiceModel();
        role.setAuthority(roleChangeBindingModel.getRole());



        if(role.getAuthority() != null){
            this.userService.addRoleToUser(user, role);
        }

        modelAndView.addObject("users", this.userService
        .findAllUsers());
        return view("admin/all-users", modelAndView);
    }

}
