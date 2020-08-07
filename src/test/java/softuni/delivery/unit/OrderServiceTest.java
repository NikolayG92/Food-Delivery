package softuni.delivery.unit;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.exceptions.UserNotFoundException;
import softuni.delivery.model.entity.*;
import softuni.delivery.model.service.*;
import softuni.delivery.model.view.CartViewModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.ProductRepository;
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
import static org.mockito.Mockito.*;

public class OrderServiceTest extends BaseTest {

    Set<Order> orders;

    @Autowired
    HttpSession httpSession;
    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AddressRepository addressRepository;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    protected void beforeEach() {
        orders = new HashSet<>();
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

    @Test
    public void deleteOrder_shouldDeleteOrderIfExists(){

        Order order = new Order();
        order.setId("1");
        Order order2 = new Order();
        order.setId("2");
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
        when(orderRepository.findAll())
                .thenReturn(orders);

        OrderServiceModel orderServiceModel = orderService.findById("1");
        orderService.deleteOrder(orderServiceModel);
        Assert.assertEquals(1, orders.size());

    }

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
        User user = new User();
        user.setId("1");

        user.setOrders(new HashSet<>());
        user.getOrders().add(new Order());
        user.getOrders().add(new Order());


        List<Order> orders = new ArrayList<>(user.getOrders());
        orders.forEach(order -> order.setUser(user));
        when(orderRepository.findAllByUser(user))
                .thenReturn(orders);

        UserServiceModel userServiceModel = this.modelMapper
                .map(user, UserServiceModel.class);
        List<Order> ordersByUser = orderService.getOrdersByUser(userServiceModel);

        assertEquals(2, ordersByUser.size());
    }


  /*  @Test
    public void createOrder_shouldCreateOrder() throws UserNotFoundException {
        OrderCreateServiceModel order = new OrderCreateServiceModel();

        List<ProductServiceModel> products = new ArrayList<>();
        products.add(new ProductServiceModel());
        products.add(new ProductServiceModel());
        order.setProducts(products);

        List<Product> productList = new ArrayList<>();
        for (ProductServiceModel product : products) {
            Product pr = this.modelMapper
                    .map(product, Product.class);
            productList.add(pr);
        }

        order.setCustomer("Ivan");
        AddressServiceModel addressServiceModel = new AddressServiceModel();
        addressServiceModel.setId("1");
        order.setAddress(addressServiceModel);

        when(productRepository.findAll())
                .thenReturn(productList);
        when(addressRepository.findById("1"))
                .thenReturn(Optional.of(this.modelMapper
                .map(addressServiceModel, Address.class)));
        when(userRepository.findByUsername("Ivan"))
                .thenReturn(Optional.of(new User()));

        when(addressRepository.findById("1"))
                .thenReturn(Optional.of(new Address()));

        CartViewModel cartViewModel = new CartViewModel();

        orderService.createOrder(order, cartViewModel, "Ivan");
        verify(orderRepository)
                .save(any());
    }*/

    @Test
    public void getAllOrders_whenExist_shouldReturnAllOrdersInDB() {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        when(orderRepository.findAll())
                .thenReturn(orders);

        List<Order> allOrders = orderService.getAllOrders();

        assertEquals(2, allOrders.size());

    }

    @Test
    public void getAllOrders_whenEmpty_shouldReturnEmptyCollection() {
        when(orderRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<Order> allOrders = orderService.getAllOrders();


        assertEquals(0, allOrders.size());

    }

    @Test
    public void getPendingOrders_whenExist_shouldReturnAllOrdersInDB() {

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setInPending(true);
        Order order1 = new Order();
        order1.setInPending(true);
        orders.add(order);
        orders.add(order1);

        when(orderRepository.findAllByInPendingIsTrue())
                .thenReturn(orders);

        List<Order> pendingOrders = orderService.getPendingOrders();

        assertEquals(2, pendingOrders.size());

    }

    @Test
    public void getPendingOrders_whenEmpty_shouldReturnEmptyCollection() {
        when(orderRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<Order> allOrders = orderService.getPendingOrders();


        assertEquals(0, allOrders.size());

    }

    @Test
    public void getAcceptedOrders_whenExist_shouldReturnAllOrdersInDB() {

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setInPending(true);
        Order order1 = new Order();
        order1.setAccepted(true);
        orders.add(order);
        orders.add(order1);

        when(orderRepository.findAllByAcceptedIsTrue())
                .thenReturn(orders);

        List<Order> acceptedOrders = orderService.getAcceptedOrders();

        assertEquals(2, acceptedOrders.size());

    }

    @Test
    public void getAcceptedOrders_whenEmpty_shouldReturnEmptyCollection() {
        when(orderRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<Order> allOrders = orderService.getAcceptedOrders();


        assertEquals(0, allOrders.size());

    }

    @Test
    public void deleteOrder_shouldRemoveOrder(){
        Order order = new Order();
        order.setInPending(true);
        order.setAccepted(false);
        order.setOrderedOn(LocalDateTime.now());
        order.setId("1");
        order.setProducts(new HashSet<>());

        User user = new User();
        user.setId("1");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setOrders(new HashSet<>());
        user.getOrders().add(order);
        user.setPhoneNumber("1234");
        user.setAuthorities(new HashSet<>());
        user.setAddresses(new HashSet<>());
        order.setUser(user);
        when(orderRepository.findById("1"))
                .thenReturn(Optional.of(order));
        when(userRepository.findById("1"))
                .thenReturn(Optional.of(user));
        OrderServiceModel orderServiceModel = orderService.findById("1");
        orderService.deleteOrder(orderServiceModel);
        verify(orderRepository)
                .delete(any());
    }
}
