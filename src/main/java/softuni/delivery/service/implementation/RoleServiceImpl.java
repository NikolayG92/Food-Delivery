package softuni.delivery.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.delivery.model.entity.Role;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.repository.RoleRepository;
import softuni.delivery.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return this.modelMapper
                .map(this.roleRepository
                .findByAuthority(authority), RoleServiceModel.class);
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository
                .findAll()
                .stream()
                .map(role -> this.modelMapper.map
                        (role, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void seedRolesInDb() {
        if (roleRepository.count() == 0) {
            roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
            roleRepository.saveAndFlush(new Role("ROLE_USER"));
        }
    }
}
