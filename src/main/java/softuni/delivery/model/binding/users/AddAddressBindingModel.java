package softuni.delivery.model.binding.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import softuni.delivery.model.entity.Town;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class AddAddressBindingModel {

    private String town;
    private String street;
    private int number;
    private String entrance;
    private int floor;
    private int apartmentNumber;

}
