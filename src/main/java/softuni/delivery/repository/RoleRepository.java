package softuni.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.delivery.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByAuthority(String authority);
}
