package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import softuni.delivery.model.entity.Town;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class AddressServiceModel extends BaseServiceModel{

    private TownServiceModel town;
    private UserServiceModel user;
    private String street;
    private int number;
    private String entrance;
    private int floor;
    private int apartmentNumber;
}
