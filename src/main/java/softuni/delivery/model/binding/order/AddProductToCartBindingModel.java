package softuni.delivery.model.binding.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.service.CategoryServiceModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
public class AddProductToCartBindingModel {

    @NotNull(message = "Quantity cannot be less than 1!")
    @Min(value = 1, message = "Quantity cannot be less than 1!")
    private int quantity;
    private String categoryId;
}
