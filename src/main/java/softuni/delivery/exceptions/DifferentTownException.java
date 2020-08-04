package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This restaurant do not deliver to this address")
public class DifferentTownException extends RuntimeException{

    public DifferentTownException(){

    }

    public DifferentTownException(String message){
        super(message);
    }
}
