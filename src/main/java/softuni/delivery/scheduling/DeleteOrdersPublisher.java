package softuni.delivery.scheduling;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DeleteOrdersPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public DeleteOrdersPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void deleteOrderPublisher(String orderId){
        DeleteProductEvent deleteProductEvent = new DeleteProductEvent(this, orderId);
        this.applicationEventPublisher.publishEvent(deleteProductEvent);
    }
}
