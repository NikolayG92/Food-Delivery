package softuni.delivery.integrational.web.controllers;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import softuni.delivery.integrational.web.WebBaseTest;
import softuni.delivery.model.binding.users.UserRegisterBindingModel;
import softuni.delivery.model.entity.Role;
import softuni.delivery.model.entity.User;
import softuni.delivery.model.service.RoleServiceModel;
import softuni.delivery.model.service.UserServiceModel;
import softuni.delivery.repository.UserRepository;
import softuni.delivery.validations.user.UserRegisterValidator;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends WebBaseTest {

    @MockBean
    protected UserRepository userRepository;
    @MockBean
    protected ModelMapper modelMapper;
    @MockBean
    protected UserRegisterValidator validator;

    @Test
    public void registerGET() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void registerConfirm_WhenUserOk_Error() throws Exception {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("name");
        userRegisterBindingModel.setPassword("1");
        userRegisterBindingModel.setEmail("email");
        User user = new User();
        user.setUsername("name");
        user.setPassword("1");
        user.setEmail("email");
        user.setAuthorities(Set.of(new Role()));
        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("name");
        userServiceModel.setPassword("1");
        userServiceModel.setEmail("email");
        userServiceModel.setAuthorities(Set.of(new RoleServiceModel()));
        Mockito.when(modelMapper.map(userRegisterBindingModel, UserServiceModel.class))
                .thenReturn(userServiceModel);
        Mockito.when(userRepository.saveAndFlush(user))
                .thenReturn(user);
        mockMvc.perform(post("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }


}
