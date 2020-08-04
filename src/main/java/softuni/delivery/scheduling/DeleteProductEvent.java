package softuni.delivery.scheduling;

import org.springframework.context.ApplicationEvent;

public class DeleteProductEvent extends ApplicationEvent {

    private final String message;

    public DeleteProductEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString() {
        return "DeleteProductEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
