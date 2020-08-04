package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Wrong input")
public class EntityNotSavedInDbException extends RuntimeException {

    public EntityNotSavedInDbException() {

    }

    public EntityNotSavedInDbException(String message) {
        super(message);

    }
}
