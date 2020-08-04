package softuni.delivery.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderServiceModel extends BaseServiceModel{

    private boolean isPending;
    private boolean accepted;
}
