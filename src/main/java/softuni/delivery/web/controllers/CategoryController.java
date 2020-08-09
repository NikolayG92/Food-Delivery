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
import softuni.delivery.model.binding.admin.CategoryAddBindingModel;
import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.model.binding.order.AddProductToCartBindingModel;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.service.*;
import softuni.delivery.validations.admin.AddCategoryValidator;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController{

    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final RestaurantService restaurantService;
    private final TownService townService;
    private final ProductService productService;
    private final AddCategoryValidator addCategoryValidator;
    private final CloudinaryService cloudinaryService;

    public CategoryController(ModelMapper modelMapper, CategoryService categoryService, RestaurantService restaurantService, TownService townService, ProductService productService, AddCategoryValidator addCategoryValidator, CloudinaryService cloudinaryService) {
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.restaurantService = restaurantService;
        this.townService = townService;
        this.productService = productService;
        this.addCategoryValidator = addCategoryValidator;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("Add Category")
    public ModelAndView addCategory(@RequestParam("id") String id,
                                    ModelAndView modelAndView,
                                    @ModelAttribute CategoryAddBindingModel categoryAddBindingModel){

        categoryAddBindingModel.setRestaurantId(id);
        modelAndView.addObject("categoryAddBindingModel", categoryAddBindingModel);

            return view("/admin/add-category", modelAndView);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addConfirm(@RequestParam("id") String id,
                             @Valid @ModelAttribute("categoryAddBindingModel")
                                     CategoryAddBindingModel categoryAddBindingModel,
                             BindingResult bindingResult,
                             ModelAndView modelAndView) throws IOException {
        addCategoryValidator.validate(categoryAddBindingModel, bindingResult);
        if(bindingResult.hasErrors()){
            categoryAddBindingModel.setRestaurantId(id);
           modelAndView.addObject("categoryAddBindingModel", categoryAddBindingModel);

           return view("/admin/add-category", modelAndView);
        }

        CategoryServiceModel category = this
                .modelMapper
                .map(categoryAddBindingModel, CategoryServiceModel.class);
        RestaurantServiceModel restaurant = this.modelMapper
                .map(this.restaurantService.findById(id),
                        RestaurantServiceModel.class);
        category.setRestaurant(restaurant);
        imgValidate(categoryAddBindingModel, category);
        this.categoryService.addCategory(category);
        return redirect("/home");

    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@RequestParam("id") String id){

        CategoryServiceModel categoryServiceModel =
                this.modelMapper
                .map(this.categoryService
                .findById(id), CategoryServiceModel.class);
        this.restaurantService
                .deleteCategoryById(id);
        return redirect(String.format("/restaurants/restaurant/?id=%s",
                categoryServiceModel.getRestaurant().getId()));
    }

    @GetMapping("/category")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Choose Products")
    public ModelAndView getCategoryById(@RequestParam("id") String id,
                                          ModelAndView modelAndView,
                                        Model model){

        if(!model.containsAttribute("addProductToCartBindingModel")){
            model.addAttribute("addProductToCartBindingModel", new AddProductToCartBindingModel());
        }
        modelAndView.addObject("category", this.categoryService.findById(id));
        modelAndView.addObject("products", this.productService
                .findAllByCategoryId(id));

        modelAndView.setViewName("categories/category-details");
        return modelAndView;
    }

    private void imgValidate(@ModelAttribute(name = "model") CategoryAddBindingModel categoryAddBindingModel, CategoryServiceModel categoryServiceModel) throws IOException {
        if (!categoryAddBindingModel.getImageUrl().isEmpty()) {
            categoryServiceModel.setImageUrl(cloudinaryService.uploadImg(categoryAddBindingModel.getImageUrl()));
        } else {
            categoryServiceModel.setImageUrl("/img/no-image-available.jpg");
        }
    }



}
