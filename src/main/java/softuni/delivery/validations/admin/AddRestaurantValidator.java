package softuni.delivery.validations.admin;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.admin.RestaurantAddBindingModel;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.validations.annotation.Validator;

import static softuni.delivery.validations.admin.AdminValidationConstants.*;

@Validator
public class AddRestaurantValidator implements org.springframework.validation.Validator{

    private final RestaurantRepository restaurantRepository;

    public AddRestaurantValidator(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RestaurantAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        RestaurantAddBindingModel restaurantAddBindingModel = (RestaurantAddBindingModel) o;

        if(restaurantAddBindingModel.getName() == null || restaurantAddBindingModel.getName().isEmpty()){
            errors.rejectValue("name", RESTAURANT_NAME_CANNOT_BE_EMPTY,
                    RESTAURANT_NAME_CANNOT_BE_EMPTY);
        }

        if(restaurantAddBindingModel.getName().length() < 2){
            errors.rejectValue("name", RESTAURANT_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS,
                    RESTAURANT_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS);
        }

        if (errors.hasErrors()){
            return;
        }


        if (this.restaurantRepository.findByName(restaurantAddBindingModel.getName()) != null) {
            errors.rejectValue(
                    "name",
                    String.format(RESTAURANT_NAME_ALREADY_EXISTS, restaurantAddBindingModel.getName()),
                    String.format(RESTAURANT_NAME_ALREADY_EXISTS, restaurantAddBindingModel.getName())
            );
        }

        if(restaurantAddBindingModel.getTown() == null || restaurantAddBindingModel.getTown().isEmpty()){
            errors.rejectValue(
                    "town",
                    CHOOSE_TOWN, CHOOSE_TOWN
            );
        }
    }
}
