package softuni.delivery.validations.admin;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.validations.annotation.Validator;
import softuni.delivery.validations.user.ValidationConstants;

import static softuni.delivery.validations.admin.AdminValidationConstants.*;

@Validator
public class AddTownValidator implements org.springframework.validation.Validator{

    private final TownRepository townRepository;

    public AddTownValidator(TownRepository townRepository) {
        this.townRepository = townRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TownAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        TownAddBindingModel townAddBindingModel = (TownAddBindingModel) o;

        if(townAddBindingModel.getName() == null || townAddBindingModel.getName().isEmpty()){
            errors.rejectValue("name", TOWN_NAME_CANNOT_BE_EMPTY, TOWN_NAME_CANNOT_BE_EMPTY);
        }

        if(townAddBindingModel.getName().length() < 3){
            errors.rejectValue("name", TOWN_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS,
                    TOWN_NAME_LENGTH_MUST_BE_MORE_THAN_3_SYMBOLS);
        }

        if (errors.hasErrors()){
            return;
        }


        if (this.townRepository.findByName(townAddBindingModel.getName()) != null) {
            errors.rejectValue(
                    "name",
                    String.format(TOWN_NAME_ALREADY_EXISTS, townAddBindingModel.getName()),
                    String.format(TOWN_NAME_ALREADY_EXISTS, townAddBindingModel.getName())
            );
        }
    }
}
