package softuni.delivery.scheduling;

import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softuni.delivery.model.entity.Order;
import softuni.delivery.model.service.OrderServiceModel;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Component
public class DeleteOrdersScheduler {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public DeleteOrdersScheduler(OrderRepository orderRepository, OrderService orderService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @Scheduled(cron = "0 23 * * * ?")
    public void deleteOrders(){
        List<OrderServiceModel> orders = this.orderRepository
                .findAll()
                .stream()
                .map(order -> {
                    OrderServiceModel orderServiceModel = this.modelMapper
                            .map(order, OrderServiceModel.class);
                    return orderServiceModel;
                }).collect(Collectors.toList());
        for (OrderServiceModel order : orders) {
                    orderService.deleteOrder(order);
            }
        }

    }



