package softuni.delivery.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import softuni.delivery.model.service.ProductServiceModel;
import softuni.delivery.service.CartService;
import softuni.delivery.service.ProductService;
import softuni.delivery.service.implementation.CartServiceImpl;

@Configuration
public class AppBeanConfiguration {

    private ProductService productService;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public CartService cartService(){
        return new CartServiceImpl(productService, modelMapper());
    }
}
