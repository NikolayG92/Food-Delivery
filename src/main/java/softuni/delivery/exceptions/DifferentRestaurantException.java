package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Product from different restaurant is trying to be added!")
public class DifferentRestaurantException extends RuntimeException{

    public DifferentRestaurantException(){

    }

    public DifferentRestaurantException(String message){
        super(message);
    }

}
