package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User was not created!")
public class UserWrongCredentialsException extends RuntimeException{

    public UserWrongCredentialsException(){

    }

    public UserWrongCredentialsException(String message){
        super(message);
    }
}
