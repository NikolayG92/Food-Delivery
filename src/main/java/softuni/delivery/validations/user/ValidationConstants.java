package softuni.delivery.validations.user;

public class ValidationConstants {

    final static String USERNAME_ALREADY_EXISTS = "Username %s already exists!";
    final static String USERNAME_CAN_NOT_BE_EMPTY = "Username can not be empty!";
    final static String FIRST_NAME_CAN_NOT_BE_EMPTY = "First name can not be empty!";
    final static String LAST_NAME_CAN_NOT_BE_EMPTY = "Last name can not be empty!";
    final static String PHONE_NUMBER_CAN_NOT_BE_EMPTY = "Phone number can not be empty!";

    final static String USERNAME_LENGTH = "Username must be between %d and %d characters long!";


    final static int USERNAME_MIN_LENGTH = 3;
    final static int USERNAME_MAX_LENGTH = 20;

    final static String PASSWORDS_DO_NOT_MATCH = "Passwords are not equals!";

    final static String EMAIL_ALREADY_EXISTS = "Email %s already exists!";
    final static String EMAIL_CANT_BE_EMPTY = "Email can not be empty!";

    final static String WRONG_PASSWORD = "Wrong password!";
    final static String PASSWORD_CANT_BE_EMPTY = "Password can not be empty!";

    final static String TOWN_CANT_BE_EMPTY = "Please choose town!";
    final static String STREET_CANT_BE_EMPTY = "Street cannot be empty!";
    final static String NUMBER_CANT_BE_ZERO = "Please enter positive number!";

}
