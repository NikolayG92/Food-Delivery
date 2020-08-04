package softuni.delivery.validations.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import softuni.delivery.model.entity.User;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.validations.annotation.Validator;
import softuni.delivery.model.binding.users.UserEditBindingModel;

import static softuni.delivery.validations.user.ValidationConstants.*;

@Validator
public class UserEditValidator implements org.springframework.validation.Validator{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEditValidator(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEditBindingModel userEditBindingModel = (UserEditBindingModel) o;

        User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElse(null);

        if (!this.bCryptPasswordEncoder.matches(userEditBindingModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue(
                    "oldPassword",
                    WRONG_PASSWORD,
                    WRONG_PASSWORD
            );
        }

        if (userEditBindingModel.getPassword() != null && !userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    PASSWORDS_DO_NOT_MATCH,
                    PASSWORDS_DO_NOT_MATCH
            );
        }

        if (!user.getEmail().equals(userEditBindingModel.getEmail()) && this.userRepository.findByEmail(userEditBindingModel.getEmail()) != null) {
            errors.rejectValue(
                    "email",
                    String.format(EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail())
            );
        }

    }
}
