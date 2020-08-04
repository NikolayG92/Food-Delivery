package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User with that username already exist")
public class  UsernameExistException extends RuntimeException{

    public UsernameExistException() {
    }
    public UsernameExistException(String message) {
        super(message);
    }
}
