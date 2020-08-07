package softuni.delivery.validations.user;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.order.AddProductToCartBindingModel;
import softuni.delivery.validations.annotation.Validator;

@Validator
public class AddProductToCartValidator implements org.springframework.validation.Validator{


    public AddProductToCartValidator(){

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return AddProductToCartBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
          AddProductToCartBindingModel addProductToCartBindingModel = (AddProductToCartBindingModel) o;

          if(addProductToCartBindingModel.getQuantity() < 1){
              errors.rejectValue("quantity",
                      "Quantity cannot be less than 1!",
                      "Quantity cannot be less than 1!");
          }

          if(errors.hasErrors()){
              return;
          }
    }
}
