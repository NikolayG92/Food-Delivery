package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.service.AddressServiceModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressServiceModel findById(String addressId) {
        return this.modelMapper
                .map(this.addressRepository
                .findById(addressId).orElse(null), AddressServiceModel.class);
    }
}
