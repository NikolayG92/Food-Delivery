package softuni.delivery.validations.admin;

import org.springframework.validation.Errors;
import softuni.delivery.model.binding.admin.CategoryAddBindingModel;
import softuni.delivery.repository.CategoryRepository;
import softuni.delivery.validations.annotation.Validator;

import static softuni.delivery.validations.admin.AdminValidationConstants.*;

@Validator
public class AddCategoryValidator implements org.springframework.validation.Validator{
    private final CategoryRepository categoryRepository;

    public AddCategoryValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CategoryAddBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
         CategoryAddBindingModel categoryAddBindingModel = (CategoryAddBindingModel) o;

         if(categoryAddBindingModel.getName() == null || categoryAddBindingModel.getName().isEmpty()){
             errors.rejectValue("name", CATEGORY_NAME_CANNOT_BE_EMPTY, CATEGORY_NAME_CANNOT_BE_EMPTY);
         }

         if(categoryAddBindingModel.getName().length() < 2){
             errors.rejectValue("name",
                     CATEGORY_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS,
                     CATEGORY_NAME_LENGTH_MUST_BE_MORE_THAN_2_SYMBOLS);
         }

         if(errors.hasErrors()){
             return;
         }
    }
}
