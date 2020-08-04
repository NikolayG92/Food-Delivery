package softuni.delivery.validations.user;

import org.springframework.validation.Errors;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.validations.annotation.Validator;
import softuni.delivery.model.binding.users.UserRegisterBindingModel;



@Validator
public class UserRegisterValidator implements org.springframework.validation.Validator{

    private final UserRepository userRepository;

    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) o;

        if (userRegisterBindingModel.getUsername() == null ||
                userRegisterBindingModel.getUsername().isEmpty()) {
            errors.rejectValue(
                    "username",
                    ValidationConstants.USERNAME_CAN_NOT_BE_EMPTY,
                    ValidationConstants.USERNAME_CAN_NOT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getPassword() == null ||
                userRegisterBindingModel.getPassword().isEmpty()) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORD_CANT_BE_EMPTY,
                    ValidationConstants.PASSWORD_CANT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getEmail() == null ||
                userRegisterBindingModel.getEmail().isEmpty()) {
            errors.rejectValue(
                    "email",
                    ValidationConstants.EMAIL_CANT_BE_EMPTY,
                    ValidationConstants.EMAIL_CANT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getFirstName() == null ||
                userRegisterBindingModel.getFirstName().isEmpty()) {
            errors.rejectValue(
                    "firstName",
                    ValidationConstants.FIRST_NAME_CAN_NOT_BE_EMPTY,
                    ValidationConstants.FIRST_NAME_CAN_NOT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getLastName() == null ||
                userRegisterBindingModel.getLastName().isEmpty()) {
            errors.rejectValue(
                    "lastName",
                    ValidationConstants.LAST_NAME_CAN_NOT_BE_EMPTY,
                    ValidationConstants.LAST_NAME_CAN_NOT_BE_EMPTY
            );
        }
        if (userRegisterBindingModel.getPhoneNumber() == null ||
                userRegisterBindingModel.getPhoneNumber().isEmpty()) {
            errors.rejectValue(
                    "phoneNumber",
                    ValidationConstants.PHONE_NUMBER_CAN_NOT_BE_EMPTY,
                    ValidationConstants.PHONE_NUMBER_CAN_NOT_BE_EMPTY
            );
        }
        if (errors.hasErrors()){
            return;
        }


        if (this.userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername()),
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername())
            );
        }

        if (userRegisterBindingModel.getUsername().length() < ValidationConstants.USERNAME_MIN_LENGTH ||
                userRegisterBindingModel.getUsername().length() > ValidationConstants.USERNAME_MAX_LENGTH) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_LENGTH, ValidationConstants.USERNAME_MIN_LENGTH,
                            ValidationConstants.USERNAME_MAX_LENGTH),
                    String.format(ValidationConstants.USERNAME_LENGTH, ValidationConstants.USERNAME_MIN_LENGTH,
                            ValidationConstants.USERNAME_MAX_LENGTH)
            );
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }

        if (this.userRepository.findByEmail(userRegisterBindingModel.getEmail()) != null) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())
            );
        }
    }
}
