package softuni.delivery.model.service;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserServiceModel extends BaseServiceModel{

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Set<AddressServiceModel> addresses;
    private Set<RoleServiceModel> authorities;

}
