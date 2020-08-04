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
import softuni.delivery.model.binding.order.OrderAddBindingModel;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.model.service.OrderCreateServiceModel;
import softuni.delivery.model.service.OrderServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.service.AddressService;
import softuni.delivery.service.OrderService;
import softuni.delivery.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrdersController extends BaseController{
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final UserService userService;

    public OrdersController(OrderService orderService, ModelMapper modelMapper, AddressService addressService, UserService userService) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.userService = userService;
    }


    @GetMapping("/create-order")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Order Details")
    public String createOrder(Model model,
                              HttpSession httpSession,
                              Principal principal) throws UserNotFoundException {

        model.addAttribute("orderAddBindingModel", new OrderAddBindingModel());
        CartViewModel cart = (CartViewModel) httpSession.getAttribute("cart");
        model.addAttribute("products", cart.getProducts());
        UserServiceModel user = this.userService
                .findByUsername(principal.getName());
        model.addAttribute("addresses", user.getAddresses());
        model.addAttribute("totalPrice", ((CartViewModel) httpSession.getAttribute("cart"))
                .getTotalPrice());

       return "user/create-order";
    }

    @PostMapping("/create-order")
    @PreAuthorize("isAuthenticated()")
    public String orderConfirm(@Valid @ModelAttribute("orderAddBindingModel")
                               OrderAddBindingModel orderAddBindingModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               HttpSession httpSession,
                               Principal principal) throws UserNotFoundException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("orderAddBindingModel", orderAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.BindingResult.orderAddBindingModel", bindingResult);
            return "user/create-order";
        }

        OrderCreateServiceModel order = this.modelMapper
                .map(orderAddBindingModel, OrderCreateServiceModel.class);
        AddressServiceModel address = this.addressService.findById(orderAddBindingModel.getAddress());
        order.setAddress(address);
        CartViewModel cartViewModel = (CartViewModel) httpSession.getAttribute("cart");
        String username = getUsername(principal);

        this.orderService.createOrder(order, cartViewModel, username);
        ((CartViewModel) httpSession.getAttribute("cart")).setProducts(new ArrayList<>());
        return "redirect:/home";


    }

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @PageTitle("All Orders")
    public ModelAndView getAllOrders(ModelAndView modelAndView){

        modelAndView.addObject("pendingOrders", this.orderService.getPendingOrders());
        modelAndView.addObject("acceptedOrders", this.orderService.getAcceptedOrders());
        modelAndView.setViewName("admin/all-orders");
        return modelAndView;
    }

    @GetMapping("/all-orders/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView changeStatus(@PathVariable("id") String id){

        OrderServiceModel orderServiceModel = this.orderService
                .findById(id);


        if(orderServiceModel.isPending()){
            this.orderService.changeStatusToAccepted(orderServiceModel);

        }else if(orderServiceModel.isAccepted()){
            this.orderService.deleteOrder(orderServiceModel);

        }
        return redirect("/orders/all-orders");
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Your Orders")
    public ModelAndView getMyOrders(ModelAndView modelAndView, Principal principal) throws UserNotFoundException {

        UserServiceModel user = this.userService
                .findByUsername(principal.getName());
        modelAndView.addObject("orders", this.orderService.getOrdersByUser(user));
        modelAndView.setViewName("user/my-orders");
        return modelAndView;
    }






}
