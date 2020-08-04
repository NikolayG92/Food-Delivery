package softuni.delivery.service;

import softuni.delivery.model.service.AddressServiceModel;

public interface AddressService {
    AddressServiceModel findById(String addressId);
}
