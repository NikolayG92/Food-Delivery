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
import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.service.CloudinaryService;
import softuni.delivery.service.RestaurantService;
import softuni.delivery.service.TownService;
import softuni.delivery.validations.admin.AddTownValidator;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/towns")
@PreAuthorize("isAuthenticated()")
public class TownsController extends BaseController{

    private final ModelMapper modelMapper;
    private final TownService townService;
    private final RestaurantService restaurantService;
    private final AddTownValidator addTownValidator;
    private final CloudinaryService cloudinaryService;

    public TownsController(ModelMapper modelMapper, TownService townService, RestaurantService restaurantService, AddTownValidator addTownValidator, CloudinaryService cloudinaryService) {
        this.modelMapper = modelMapper;
        this.townService = townService;
        this.restaurantService = restaurantService;
        this.addTownValidator = addTownValidator;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Town")
    public ModelAndView addTown(ModelAndView modelAndView,
                                @ModelAttribute TownAddBindingModel townAddBindingModel){
        modelAndView.addObject("townAddBindingModel",townAddBindingModel);
        return view("/admin/add-town", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Town")
    public ModelAndView addTownConfirm(@Valid @ModelAttribute("townAddBindingModel")
                                         TownAddBindingModel townAddBindingModel,
                                 BindingResult bindingResult,
                                       ModelAndView modelAndView) throws IOException {

        addTownValidator.validate(townAddBindingModel, bindingResult);

        if(bindingResult.hasErrors()){
            modelAndView.addObject(townAddBindingModel);
            return view("/admin/add-town", modelAndView);
        }


        TownServiceModel townServiceModel = this.modelMapper
                .map(townAddBindingModel, TownServiceModel.class);
        imgValidate(townAddBindingModel, townServiceModel);
        this.townService.addTown(townServiceModel);
        return redirect("/home");
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String delete(@RequestParam("id") String id){
        this.townService.deleteTownById(id);
        return "redirect:/home";
    }

    @GetMapping("/town")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Restaurants")
    public ModelAndView getTownById(@RequestParam("id") String id,
                                    ModelAndView modelAndView){

        modelAndView.addObject("town", this.townService.findById(id));
        modelAndView.addObject("restaurants", this.restaurantService
                .findRestaurantsByTownId(id));
        modelAndView.setViewName("towns/town-restaurants");
        return modelAndView;
    }

    private void imgValidate(@ModelAttribute(name = "model") TownAddBindingModel model, TownServiceModel townServiceModel) throws IOException {
        if (!model.getImageUrl().isEmpty()) {
            townServiceModel.setImageUrl(cloudinaryService.uploadImg(model.getImageUrl()));
        } else {
            townServiceModel.setImageUrl("/img/no-image-available.jpg");
        }
    }
}
