package softuni.delivery.validations.user;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.users.AddAddressBindingModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.validations.annotation.Validator;

import static softuni.delivery.validations.user.ValidationConstants.*;

@Validator
public class AddAddressValidator implements org.springframework.validation.Validator{

    private final AddressRepository addressRepository;

    public AddAddressValidator(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AddAddressBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AddAddressBindingModel addAddressBindingModel = (AddAddressBindingModel) o;

        if(addAddressBindingModel.getTown() == null || addAddressBindingModel.getTown().isEmpty()){
            errors.rejectValue("town", TOWN_CANT_BE_EMPTY,
                    TOWN_CANT_BE_EMPTY);
        }

        if(addAddressBindingModel.getStreet() == null || addAddressBindingModel.getStreet().isEmpty()){
            errors.rejectValue("street",
                    STREET_CANT_BE_EMPTY, STREET_CANT_BE_EMPTY);
        }

        if(addAddressBindingModel.getNumber() <= 0){
            errors.rejectValue("number",
                    NUMBER_CANT_BE_ZERO, NUMBER_CANT_BE_ZERO);
        }

        if (errors.hasErrors()) {
            return;
        }
    }
}
