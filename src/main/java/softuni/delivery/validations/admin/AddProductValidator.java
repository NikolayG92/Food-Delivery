package softuni.delivery.validations.admin;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.admin.ProductAddBindingModel;
import softuni.delivery.model.entity.Product;
import softuni.delivery.repository.ProductRepository;
import softuni.delivery.validations.annotation.Validator;

import java.math.BigDecimal;

import static softuni.delivery.validations.admin.AdminValidationConstants.*;

@Validator
public class AddProductValidator implements org.springframework.validation.Validator{

    private final ProductRepository productRepository;

    public AddProductValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductAddBindingModel productAddBindingModel = (ProductAddBindingModel) o;

        if(productAddBindingModel.getName() == null || productAddBindingModel.getName().isEmpty()){
             errors.rejectValue("name", PRODUCT_NAME_CANNOT_BE_EMPTY,
                     PRODUCT_NAME_CANNOT_BE_EMPTY);
        }
        if(productAddBindingModel.getName().length() < 2){
            errors.rejectValue("name", PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS,
                    PRODUCT_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS);
        }

        if(productAddBindingModel.getSize() <= 0){
            errors.rejectValue("size", PRODUCT_SIZE_MUST_BE_POSITIVE,
                    PRODUCT_SIZE_MUST_BE_POSITIVE);
        }

/*        if(productAddBindingModel.getPrice().compareTo(BigDecimal.ZERO) < 0){
           errors.rejectValue("price", PRODUCT_PRICE_MUST_BE_POSITIVE,
                   PRODUCT_PRICE_MUST_BE_POSITIVE);
        }*/

        if(errors.hasErrors()){
            return;
        }
    }
}
