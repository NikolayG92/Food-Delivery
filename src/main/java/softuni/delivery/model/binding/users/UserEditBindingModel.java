package softuni.delivery.model.binding.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class UserEditBindingModel {

    private String username;
    @NotEmpty(message = "Please enter your old password")
    private String oldPassword;
    @NotEmpty(message = "Please enter your new password")
    @Length(min = 4, message = "Password must be more than 4 characters!")
    private String password;
    private String confirmPassword;
    @Length(min = 8, message = "Please enter valid phone number!")
    private String phoneNumber;
    @Email
    @Length(min = 4, message = "Please enter valid email")
    private String email;
}
