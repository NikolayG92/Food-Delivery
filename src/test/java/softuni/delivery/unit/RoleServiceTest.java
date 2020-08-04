package softuni.delivery.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import softuni.delivery.BaseTest;
import softuni.delivery.model.entity.Role;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.repository.RoleRepository;
import softuni.delivery.service.RoleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class RoleServiceTest extends BaseTest {

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @Test
    public void seedRolesInDb_whenRepositoryIsEmpty_shouldSeedAllRoles() {

        List<Role> roles = new ArrayList<>();
        roles.add(new Role());
        roles.add(new Role());
        roles.add(new Role());

        when(roleRepository.count()).thenReturn(0L);

        roleService.seedRolesInDb();

        assertEquals(3, roles.size());
    }

    @Test
    public void seedRolesInDb_whenRepositoryNotEmpty_shouldNotSeedRoles() {

        List<Role> roles = new ArrayList<>();

        when(roleRepository.count()).thenReturn(3L);

        roleService.seedRolesInDb();

        assertEquals(0, roles.size());
    }
    // ==================
    @Test
    public void findAllRoles_whenAnyRolesInDb_shouldReturnAllCorrect() {

        List<Role> roles;
        roles = Arrays.asList(new Role("ROLE_MODERATOR"), new Role("ROLE_ADMIN"), new Role("ROLE_USER"));
        when(roleRepository.findAll()).thenReturn(roles);

        Set<RoleServiceModel> roleServiceModels = roleService.findAllRoles();

        assertEquals(roles.size(), roleServiceModels.size());
    }

    @Test
    public void findAllRoles_whenNoRolesInDb_shouldReturnEmptySet(){

        List<Role> roles = new ArrayList<>();

        when(roleRepository.findAll()).thenReturn(roles);

        Set<RoleServiceModel> roleServiceModels = roleService.findAllRoles();

        assertEquals(0, roleServiceModels.size());
    }

    @Test
    public void findByAuthority_whenAuthorityExist_shouldReturnCorrectRole(){

        when(roleRepository.findByAuthority("ROLE_MODERATOR"))
                .thenReturn((new Role("ROLE_MODERATOR")));

        RoleServiceModel roleServiceModel = roleService.findByAuthority("ROLE_MODERATOR");

        assertEquals("ROLE_MODERATOR", roleServiceModel.getAuthority());
    }

    @Test
    public void findByAuthority_whenAuthorityNotExist_shouldThrowException() {

        when(roleRepository.findByAuthority("ROLE_MODERATOR"))
                .thenReturn(null);

        assertThrows(Exception.class, () -> roleService.findByAuthority("ROLE_MODERATOR"));
    }


}
