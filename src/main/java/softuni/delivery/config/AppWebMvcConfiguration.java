package softuni.delivery.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import softuni.delivery.web.interceptors.FavIconInterceptor;
import softuni.delivery.web.interceptors.TitleInterceptor;

@Configuration
@AllArgsConstructor
public class AppWebMvcConfiguration implements WebMvcConfigurer {

    private final TitleInterceptor titleInterceptor;
    private final FavIconInterceptor favIconInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(titleInterceptor);
        registry.addInterceptor(favIconInterceptor);
    }
}
