package softuni.delivery.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import softuni.delivery.config.annotations.PageTitle;
import softuni.delivery.exceptions.DifferentRestaurantException;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.binding.order.AddProductToCartBindingModel;
import softuni.delivery.model.service.CategoryServiceModel;
import softuni.delivery.model.service.RestaurantServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.ProductViewModel;
import softuni.delivery.service.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final OrderService orderService;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final CartService cartService;
    private final CategoryService categoryService;

    public CartController(ModelMapper modelMapper, ProductService productService, OrderService orderService, RestaurantService restaurantService, UserService userService
            , CartService cartService, CategoryService categoryService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.orderService = orderService;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.cartService = cartService;
        this.categoryService = categoryService;
    }


    @GetMapping("/my-cart")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Your Cart")
    public ModelAndView getCart(ModelAndView modelAndView, HttpSession httpSession,
                                Principal principal) throws UserNotFoundException {

        CartViewModel cart = (CartViewModel)
                httpSession.getAttribute("cart");

        modelAndView.addObject("cart", cart);
        modelAndView.addObject("products", cart.getProducts());
        modelAndView.addObject("totalPrice", findTotalPrice(cart.getProducts()));
        UserServiceModel user = this.userService
                .findByUsername(principal.getName());
        modelAndView.addObject("addresses", user.getAddresses());
        modelAndView.setViewName("user/cart");
        return modelAndView;
    }

    @PostMapping("/add-product")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addProduct(
                                   AddProductToCartBindingModel addProductToCartBindingModel,
                                   @RequestParam("id") String id,
                                   ModelAndView modelAndView,
                                   HttpSession httpSession,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductToCartBindingModel", addProductToCartBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.BindingResult.addProductToCartBindingModel", bindingResult);
            modelAndView.addObject("addProductToCartBindingModel", addProductToCartBindingModel);
            return view("/categories/category-details", modelAndView);
        }
        ProductViewModel product = this.modelMapper
                .map(this.productService.getProductById(id), ProductViewModel.class);
        product.setQuantity(addProductToCartBindingModel.getQuantity());

        CategoryServiceModel categoryServiceModel = this.modelMapper
                .map(product.getCategory(), CategoryServiceModel.class);
        categoryServiceModel.setRestaurant(restaurantService.findByCategoryId(categoryServiceModel.getId()));
        RestaurantServiceModel restaurantServiceModel = categoryServiceModel.getRestaurant();
        CartViewModel cart = (CartViewModel) httpSession.getAttribute("cart");
        if (product.getQuantity() > 0) {
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));

            product.setTotalPrice(totalPrice);
            boolean isInCart = false;
            if (cart.getProducts().size() > 0) {
                CategoryServiceModel firstProductCategory = this.modelMapper
                        .map(cart.getProducts().get(0).getCategory(), CategoryServiceModel.class);
                firstProductCategory.setRestaurant(restaurantService.findByCategoryId(firstProductCategory.getId()));
                if (!firstProductCategory.getRestaurant().getId().equals(restaurantServiceModel.getId())) {
                    throw new DifferentRestaurantException("You have products from other restaurant in your cart!");
                }
                for (ProductViewModel productViewModel : ((CartViewModel) httpSession.getAttribute("cart")).getProducts()) {
                    if (productViewModel.getName().equals(product.getName())) {
                        productViewModel.setQuantity(productViewModel.getQuantity() + product.getQuantity());
                        productViewModel.setTotalPrice(productViewModel.getPrice()
                                .multiply(BigDecimal.valueOf(productViewModel.getQuantity())));
                        isInCart = true;
                    }


                }

            }
            if (isInCart) {
                return redirect(String.format("/categories/category/?id=%s",
                        product.getCategory().getId()));
            } else {
                cart.getProducts().add(product);
            }
        }
        httpSession.removeAttribute("cart");
        httpSession.setAttribute("cart", cart);
        ((CartViewModel) httpSession.getAttribute("cart")).setTotalPrice
                (findTotalPrice(((CartViewModel) httpSession.getAttribute("cart")).getProducts()));

        modelAndView.addObject("products", cart.getProducts());
        modelAndView.addObject("totalPrice", ((CartViewModel) httpSession.getAttribute("cart"))
                .getTotalPrice());
        return redirect(String.format("/categories/category/?id=%s",
                product.getCategory().getId()));

    }

    @GetMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView deleteProduct(@RequestParam("id") String id,
                                      HttpSession httpSession) {
        ProductViewModel product = this.modelMapper
                .map(this.productService.getProductById(id), ProductViewModel.class);
        CartViewModel cart = ((CartViewModel) httpSession.getAttribute("cart"));
        this.cartService.removeProduct(product, cart);
        httpSession.removeAttribute("cart");
        httpSession.setAttribute("cart", cart);
        return redirect("/cart/my-cart");
    }


    public BigDecimal findTotalPrice(List<ProductViewModel> products) {

        final BigDecimal[] totalPrice = {BigDecimal.ZERO};
        products.forEach(product -> {
            totalPrice[0] = totalPrice[0].add(product.getPrice().
                    multiply(BigDecimal.valueOf(product.getQuantity())));
        });

        return totalPrice[0];
    }
}
