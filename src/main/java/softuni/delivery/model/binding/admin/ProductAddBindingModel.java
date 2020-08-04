package softuni.delivery.model.binding.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProductAddBindingModel {


    private String name;
    private int size;
    private String description;
    @DecimalMin(value = "0.01", message = "Price can not be negative!")
    @NotNull(message = "Please enter price!")
    private BigDecimal price;
    private MultipartFile imageUrl;
    private String categoryId;


}
