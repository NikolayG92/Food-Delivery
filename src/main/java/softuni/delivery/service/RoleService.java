package softuni.delivery.service;

import softuni.delivery.model.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    RoleServiceModel findByAuthority(String authority);

    Set<RoleServiceModel>findAllRoles();

    void seedRolesInDb();
}
