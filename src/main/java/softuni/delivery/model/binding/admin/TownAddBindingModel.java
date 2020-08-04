package softuni.delivery.model.binding.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@NoArgsConstructor
@Getter
@Setter
public class TownAddBindingModel {

    private String name;
    private MultipartFile imageUrl;
}
