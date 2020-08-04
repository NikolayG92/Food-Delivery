package softuni.delivery.model.binding.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
public class AddProductToCartBindingModel {

    @Min(value = 1, message = "Quantity cannot be less than 1!")
    private int quantity;
}
