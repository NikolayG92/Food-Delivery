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
import softuni.delivery.model.binding.admin.RestaurantAddBindingModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.service.CategoryService;
import softuni.delivery.service.CloudinaryService;
import softuni.delivery.service.RestaurantService;
import softuni.delivery.service.TownService;
import softuni.delivery.validations.admin.AddRestaurantValidator;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/restaurants")
@PreAuthorize("isAuthenticated()")
public class RestaurantController extends BaseController{

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;
    private final TownService townService;
    private final TownRepository townRepository;
    private final CategoryService categoryService;
    private final AddRestaurantValidator addRestaurantValidator;
    private final CloudinaryService cloudinaryService;

    public RestaurantController(RestaurantService restaurantService, ModelMapper modelMapper, TownService townService, TownRepository townRepository, CategoryService categoryService, AddRestaurantValidator addRestaurantValidator, CloudinaryService cloudinaryService) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.townRepository = townRepository;
        this.categoryService = categoryService;
        this.addRestaurantValidator = addRestaurantValidator;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Restaurant")
    public ModelAndView addRestaurant(ModelAndView modelAndView,
                                      @ModelAttribute RestaurantAddBindingModel restaurantAddBindingModel){
        modelAndView.addObject("restaurantAddBindingModel", restaurantAddBindingModel);
        modelAndView.addObject("towns", this.townService.findAllTowns());

        return view("/admin/add-restaurant", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addConfirm(@Valid @ModelAttribute("restaurantAddBindingModel")
                             RestaurantAddBindingModel restaurantAddBindingModel,
                             BindingResult bindingResult,
                                   ModelAndView modelAndView) throws IOException {
        addRestaurantValidator.validate(restaurantAddBindingModel, bindingResult);
        if(bindingResult.hasErrors()){
            modelAndView.addObject(restaurantAddBindingModel);
            return view("/admin/add-restaurant", modelAndView);
        }

        RestaurantServiceModel restaurant = this
                .modelMapper
                .map(restaurantAddBindingModel, RestaurantServiceModel.class);
        TownServiceModel town = this.townService.findByName(restaurantAddBindingModel.getTown());
        restaurant.setTown(town);
        imgValidate(restaurantAddBindingModel, restaurant);
        this.restaurantService.addRestaurant(restaurant);

        return redirect("/home");

    }

    @GetMapping("/restaurant")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Categories")
    public ModelAndView getRestaurantById(@RequestParam("id") String id,
                                    ModelAndView modelAndView){

        modelAndView.addObject("restaurant", this.restaurantService.findById(id));
        modelAndView.addObject("categories", this.categoryService
                .findCategoriesByRestaurantId(id));
        modelAndView.setViewName("restaurants/restaurant-details");
        return modelAndView;
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String delete(@RequestParam("id") String id) {

        this.townService.deleteRestaurantById(id);

        return "redirect:/home";

    }

    private void imgValidate(@ModelAttribute(name = "model") RestaurantAddBindingModel restaurantAddBindingModel, RestaurantServiceModel restaurantServiceModel) throws IOException {
        if (!restaurantAddBindingModel.getImageUrl().isEmpty()) {
            restaurantServiceModel.setImageUrl(cloudinaryService.uploadImg(restaurantAddBindingModel.getImageUrl()));
        } else {
            restaurantServiceModel.setImageUrl("/img/no-image-available.jpg");
        }
    }

}