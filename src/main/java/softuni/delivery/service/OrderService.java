package softuni.delivery.service;

import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.Order;
import softuni.delivery.model.service.OrderCreateServiceModel;
import softuni.delivery.model.service.OrderServiceModel;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.model.view.OrderViewModel;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

public interface OrderService {


    void createOrder(OrderCreateServiceModel orderViewModel, CartViewModel cart, String username) throws UserNotFoundException;

    List<Order> getAllOrders();

    List<Order> getPendingOrders();

    List<Order> getAcceptedOrders();

    List<Order> getOrdersByUser(UserServiceModel user);

    OrderServiceModel findById(String id);

    void deleteOrder(OrderServiceModel orderServiceModel);

    void changeStatusToAccepted(OrderServiceModel orderServiceModel);

}
