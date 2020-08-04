package softuni.delivery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entity not found.")
public class EntityNotFoundException extends RuntimeException {


    public EntityNotFoundException() {

    }

    public EntityNotFoundException(String message) {
        super(message);

    }

}
