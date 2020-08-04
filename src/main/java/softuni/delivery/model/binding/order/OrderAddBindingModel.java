package softuni.delivery.model.binding.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.service.AddressServiceModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class OrderAddBindingModel {

    @NotEmpty(message = "Please tell us where to deliver your food")
    private String address;
}
