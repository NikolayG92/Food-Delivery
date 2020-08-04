package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.exceptions.ActiveOrderException;
import softuni.delivery.exceptions.EntityNotFoundException;
import softuni.delivery.model.binding.admin.TownAddBindingModel;
import softuni.delivery.model.entity.Address;
import softuni.delivery.model.entity.Restaurant;
import softuni.delivery.model.entity.Town;
import softuni.delivery.model.service.TownServiceModel;
import softuni.delivery.model.view.TownViewModel;
import softuni.delivery.repository.AddressRepository;
import softuni.delivery.repository.OrderRepository;
import softuni.delivery.repository.RestaurantRepository;
import softuni.delivery.repository.TownRepository;
import softuni.delivery.service.TownService;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, RestaurantRepository restaurantRepository, AddressRepository addressRepository, OrderRepository orderRepository) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public TownServiceModel findByName(String name) {
        TownServiceModel townServiceModel = this
                .modelMapper
                .map(this.townRepository.findByName(name), TownServiceModel.class);
        if(townServiceModel != null){
            return townServiceModel;
        }else{
            throw new EntityNotFoundException("Town with that name doesn't exist!");
        }

    }

    @Override
    public void addTown(TownServiceModel townServiceModel) {


        this.townRepository
                .saveAndFlush(this.modelMapper
                .map(townServiceModel, Town.class));
    }

    @Override
    public List<TownViewModel> findAllTowns() {

     return this.townRepository.findAll()
                .stream()
                .map(town -> {
                    TownViewModel townViewModel = this.modelMapper
                            .map(town, TownViewModel.class);
                    return townViewModel;
                }).collect(Collectors.toList());

    }

    @Override
    public Town findById(String id) {

        return this.townRepository
                .findById(id).orElse(null);
    }

    @Override
    public void addRestaurant(Town town, Restaurant restaurant) {


        this.townRepository.getOne(town.getId())
                .getRestaurants().add(restaurant);
    }

    @Override
    public void deleteRestaurantById(String id) {

        if(this.orderRepository.findAllByRestaurant(this.restaurantRepository
        .findById(id).orElse(null)).size() == 0){
        this.restaurantRepository
                .findById(id).ifPresent(restaurant -> this.townRepository
                .findById(restaurant.getTown().getId()).ifPresent(town -> town.getRestaurants().remove(restaurant)));
        this.restaurantRepository.deleteById(id);
        }else {
            throw new ActiveOrderException("This restaurant have active order!");
        }
    }

    @Override
    public void deleteTownById(String id) {

        List<Address> addresses = this.addressRepository
                .findAllByTown(this.townRepository
                .findById(id).orElse(null));
        addresses.forEach(address -> {
            if(this.orderRepository
            .findAllByAddress(address).size() > 0){
                throw new ActiveOrderException("This town have active orders!");
            }
        });
        this.townRepository
                .findById(id)
                .ifPresent(town -> {
                    this.addressRepository
                            .findAllByTown(town)
                            .forEach(address -> {
                                address.getUser()
                                        .getAddresses()
                                        .remove(address);
                                this.addressRepository
                                        .delete(address);
                            });
                });

        this.townRepository.deleteById(id);
    }

}
