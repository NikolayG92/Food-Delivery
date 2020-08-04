package softuni.delivery.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Order;
import softuni.delivery.model.entity.Product;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.*;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.service.OrderService;
import softuni.delivery.service.UserService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest extends BaseTest {

    List<Order> orders;

    @Autowired
    HttpSession httpSession;
    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AddressRepository addressRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Override
    protected void beforeEach() {
        orders = new ArrayList<>();
    }

    private Set<Product> getProducts() {
        Product pizza = new Product();
        pizza.setName("Peperoni");
        return new HashSet<>(Set.of(pizza));
    }

    private List<Order> getOrders() {
        User user = new User();
        user.setUsername("Ivan");
        Order order = new Order();
        order.setUser(user);
        order.setProducts(getProducts());
        return new ArrayList<>(List.of(order));
    }
    @Test
    public void changeStatusToAccepted_shouldWorkIfStatusIsPending(){
        Order order = new Order();
        order.setId("1");
        order.setInPending(true);
        order.setAccepted(false);
        order.setOrderedOn(LocalDateTime.now());
        when(orderRepository.findById("1"))
                .thenReturn(Optional.of(order));

        OrderServiceModel orderServiceModel = orderService.findById("1");

        orderService.changeStatusToAccepted(orderServiceModel);

        Assert.assertTrue(order.isAccepted());

    }

  /*  @Test
    public void deleteOrder_shouldDeleteOrderIfExists(){

        Order order = new Order();
        order.setId("1");
        order.setInPending(true);
        order.setAccepted(false);
        order.setOrderedOn(LocalDateTime.now());
        when(orderRepository.findById("1"))
                .thenReturn(Optional.of(order));
        orderRepository.saveAndFlush(order);

        List<Order> orders = this.orderRepository.findAll();
        Assert.assertEquals(1, orders.size());

    }*/

    @Test
    public void getOrderById_whenOrderExists_shouldReturnIt() {
        Order order = new Order();
        order.setId("1");
        when(orderRepository.findById("1"))
                .thenReturn(Optional.of(order));


        OrderServiceModel orderServiceModel = orderService.findById("1");


        assertEquals("1", orderServiceModel.getId());
    }

    @Test
    public void getOrderById_whenOrderDoNotExists_shouldThrowException() {
        Order order = new Order();
        order.setId("1");


        assertThrows(EntityNotFoundException.class, () ->
                orderService.findById(order.getId()), order.getId());
    }

    @Test
    public void getOrdersByUser_whenAllDataIsValidShouldReturnOrders() throws UserNotFoundException {
        User user=new User();
        User user2=new User();
        user2.setUsername("Gosho");
        user.setUsername("Mitko");
        Order order1=new Order();
        order1.setUser(user);
        Order order2=new Order();
        order2.setUser(user);
        Order order3=new Order();
        order3.setUser(user2);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        Mockito.when(orderRepository.findAllByUser(user))
                .thenReturn(orders.stream().filter(o->o.getUser().getUsername().equals("Mitko")).collect(Collectors.toList()));
        UserServiceModel userServiceModel = userService.findByUsername("Mitko");
        List<Order>actualOrders=orderService.getOrdersByUser(userServiceModel);
        assertEquals(2,actualOrders.size());
    }

    @Test
    public void getAllOrders_shouldReturnAllOrders() {
        List<Order> orders = getOrders();

        when(orderRepository.findAll())
                .thenReturn(orders);

        List<Order> allOrders = orderService.getAllOrders();

        assertEquals(orders.size(), allOrders.size());
        assertEquals(orders.get(0).getUser().getUsername(), allOrders.get(0).getUser().getUsername());
    }

  /*  @Test
    public void createOrder_shouldCreateOrder() throws UserNotFoundException {
        OrderCreateServiceModel order = new OrderCreateServiceModel();

        List<ProductServiceModel> products = new ArrayList<>();
        products.add(new ProductServiceModel());
        products.add(new ProductServiceModel());
        order.setProducts(products);

        order.setCustomer("Ivan");
        AddressServiceModel addressServiceModel = new AddressServiceModel();
        addressServiceModel.setId("1");
        order.setAddress(addressServiceModel);

        when(userRepository.findByUsername("Ivan"))
                .thenReturn(Optional.of(new User()));

        when(addressRepository.findById("1"))
                .thenReturn(Optional.of(new Address()));

        CartViewModel cartViewModel = new CartViewModel();

        orderService.createOrder(order, cartViewModel, "Ivan");
        verify(orderRepository)
                .save(any());
    }*/
}
