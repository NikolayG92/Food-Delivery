package softuni.delivery.model.binding.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class CategoryAddBindingModel {

    private String name;
    private MultipartFile imageUrl;
    private String restaurantId;

}
