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
import softuni.delivery.model.binding.admin.ProductAddBindingModel;
import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.service.*;
import softuni.delivery.validations.admin.AddProductValidator;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/products")
@PreAuthorize("hasRole('ROLE_MODERATOR')")
public class ProductsController extends BaseController{


    private final TownService townService;
    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AddProductValidator addProductValidator;
    private final CloudinaryService cloudinaryService;

    public ProductsController(TownService townService, RestaurantService restaurantService, ModelMapper modelMapper, CategoryService categoryService, ProductService productService, AddProductValidator addProductValidator, CloudinaryService cloudinaryService) {
        this.townService = townService;
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.productService = productService;
        this.addProductValidator = addProductValidator;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/add")
    @PageTitle("Add Product")
    public ModelAndView addProduct(@RequestParam("id") String id,
                                   Model model, ModelAndView modelAndView){

        if(!model.containsAttribute("productAddBindingModel")) {
            ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
            productAddBindingModel.setCategoryId(id);
            model.addAttribute("productAddBindingModel", productAddBindingModel);

        }
        modelAndView.addObject("towns", this.townService.findAllTowns());
        modelAndView.addObject("restaurants", this.restaurantService
                .findAllRestaurants());
        modelAndView.addObject("categories", this.categoryService
                .findAllCategories());

        return view("admin/add-product");
    }

    @PostMapping("/add")
    public String addConfirm(@RequestParam("id") String id,
                             @Valid @ModelAttribute("productAddBindingModel")
                                     ProductAddBindingModel productAddBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) throws IOException {

        addProductValidator.validate(productAddBindingModel, bindingResult);
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.BindingResult.productAddBindingModel", bindingResult);
            return "admin/add-product";
        }

        ProductServiceModel product = this
                .modelMapper
                .map(productAddBindingModel, ProductServiceModel.class);
        CategoryServiceModel category = this
        .modelMapper.map(this.categoryService
                .findById(id), CategoryServiceModel.class);
        product.setCategory(category);
        imgValidate(productAddBindingModel, product);
        this.productService.addProduct(product);
        return "redirect:/home";

    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@RequestParam("id") String id){

        Product product = this.modelMapper
        .map(this.productService
                .getProductById(id), Product.class);
        this.categoryService
                .deleteProductById(id);
        return redirect(String.format("/categories/category/?id=%s", product.getCategory().getId()));
    }

    private void imgValidate(@ModelAttribute(name = "model") ProductAddBindingModel productAddBindingModel, ProductServiceModel productServiceModel) throws IOException {
        if (!productAddBindingModel.getImageUrl().isEmpty()) {
            productServiceModel.setImageUrl(cloudinaryService.uploadImg(productAddBindingModel.getImageUrl()));
        } else {
            productServiceModel.setImageUrl("/img/no-image-available.jpg");
        }
    }
}
