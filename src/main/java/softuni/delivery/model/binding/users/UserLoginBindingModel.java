package softuni.delivery.model.binding.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
public class UserLoginBindingModel {

    @Length(min = 4, message = "Username length must be more than 4 characters!")
    private String username;
    @Length(min = 4, message = "Password length must be more than 4 characters!")
    private String password;
}
