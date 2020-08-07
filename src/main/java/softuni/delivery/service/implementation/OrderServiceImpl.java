package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.exceptions.DifferentTownException;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.*;
import softuni.delivery.model.service.OrderCreateServiceModel;
import softuni.delivery.model.service.OrderServiceModel;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.OrderViewModel;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.service.AddressService;
import softuni.delivery.service.OrderService;
import softuni.delivery.service.UserService;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final RestaurantRepository restaurantRepository;

    public OrderServiceImpl(ModelMapper modelMapper, OrderRepository orderRepository, UserService userService, AddressService addressService, RestaurantRepository restaurantRepository) {
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.addressService = addressService;
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    public void createOrder(OrderCreateServiceModel order, CartViewModel cart, String username) throws UserNotFoundException {

        List<ProductServiceModel> products = cart.getProducts()
                .stream()
                .map(productViewModel -> {
                    ProductServiceModel product = this.modelMapper
                            .map(productViewModel, ProductServiceModel.class);

                    return product;
                }).collect(Collectors.toList());

        order.setProducts(products);
        order.setTotalPrice(cart.getTotalPrice());
        UserServiceModel user = this.userService
                .findByUsername(username);
        Order finalOrder= this.modelMapper
                .map(order, Order.class);
        finalOrder.setOrderedOn(LocalDateTime.now());
        finalOrder.setInPending(true);
        finalOrder.setAccepted(false);
        finalOrder.setUser(this.modelMapper
        .map(user, User.class));
        finalOrder.setAddress(this.modelMapper
        .map(order.getAddress(), Address.class));
        Restaurant restaurant = this.restaurantRepository
                .findByCategory(this.modelMapper
                        .map(order.getProducts().get(0).getCategory(), Category.class));
        finalOrder.setRestaurant(restaurant);
        if(!restaurant.getTown().getId().equals(finalOrder.getAddress().getTown().getId())){
            throw new DifferentTownException("This restaurant do not deliver to your address");
        }else{
            this.orderRepository.save(finalOrder);
        }
    }

    @Override
    public List<Order> getAllOrders() {

        return this.orderRepository.findAll();
    }

    @Override
    public List<Order> getPendingOrders() {
        return this.orderRepository.findAllByInPendingIsTrue();
    }

    @Override
    public List<Order> getAcceptedOrders() {
        return this.orderRepository.findAllByAcceptedIsTrue();
    }

    @Override
    public List<Order> getOrdersByUser(UserServiceModel user) {

        return this.orderRepository.findAllByUser(this.modelMapper
        .map(user, User.class));
    }

    @Override
    public OrderServiceModel findById(String id) {

        if(orderRepository.findById(id).isEmpty()){
            throw new EntityNotFoundException("Order not found!");
        }
        return this.modelMapper
        .map(this.orderRepository
                .findById(id).orElse(null), OrderServiceModel.class);
    }

    @Override
    public void deleteOrder(OrderServiceModel orderServiceModel) {


        Order order = this.orderRepository
                .findById(orderServiceModel.getId()).orElse(null);

        Set<Product> products = order.getProducts();
        order.getProducts().removeAll(products);
        User user = order.getUser();
        user.getOrders().remove(order);
        order.setAddress(null);
        order.setRestaurant(null);
        order.setUser(null);
        this.orderRepository.saveAndFlush(order);
        this.orderRepository.delete(order);

    }

    @Override
    public void changeStatusToAccepted(OrderServiceModel orderServiceModel) {
        Order order = this.orderRepository
                .findById(orderServiceModel.getId()).orElse(null);
        if (order != null) {

                order.setInPending(false);
                order.setAccepted(true);
                this.orderRepository.saveAndFlush(order);

    }

    }



}
