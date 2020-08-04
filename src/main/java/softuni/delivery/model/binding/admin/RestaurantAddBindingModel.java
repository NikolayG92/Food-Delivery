package softuni.delivery.model.binding.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;
import softuni.delivery.model.entity.Town;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantAddBindingModel {

    private String name;
    private String town;
    private String description;
    private MultipartFile imageUrl;
}
