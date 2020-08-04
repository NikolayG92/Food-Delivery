package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Active order")
public class ActiveOrderException extends RuntimeException{

    public ActiveOrderException(){
    }

    public ActiveOrderException(String message){
        super(message);
    }
}
